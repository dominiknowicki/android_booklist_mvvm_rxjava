package pl.dn.booklist.data.remote

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.platform.Platform
import pl.dn.booklist.BuildConfig
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://flashore.webd.pl/"

class RetrofitImpl {

    var apiInterface: ApiInterface

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        client.addInterceptor(
            LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build()
        )
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val request = original.getRequestWithHeaders()
                return@addNetworkInterceptor chain.proceed(request)
            }

        val objectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        apiInterface = retrofit.create(ApiInterface::class.java)
    }

    private fun Request.getRequestWithHeaders(): Request {
        val builder = newBuilder()
            .header("user-agent", "")
            .header("Content-Type", "application/json; UTF-8")
        return builder.method(method(), body()).build()
    }
}