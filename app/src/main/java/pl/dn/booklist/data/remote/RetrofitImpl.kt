package pl.dn.booklist.data.remote

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.platform.Platform
import pl.dn.booklist.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

const val BOOKS_BASE_URL = "http://flashore.webd.pl/"
const val WIKI_API_BASE_URL = "https://en.wikipedia.org/"

class RetrofitImpl {

    var apiInterface: ApiInterface
    var mediaApiInterface: MediaApiInterface

    init {
        apiInterface = getRetrofitBuilder(BOOKS_BASE_URL).build().create(ApiInterface::class.java)
        mediaApiInterface = getRetrofitBuilder(WIKI_API_BASE_URL).build().create(MediaApiInterface::class.java)
    }

    private fun getRetrofitBuilder(baseUrl: String): Retrofit.Builder {
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

        return retrofit2.Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
    }

    private fun Request.getRequestWithHeaders(): Request {
        val builder = newBuilder()
            .header("user-agent", "")
            .header("Content-Type", "application/json; UTF-8")
        return builder.method(method(), body()).build()
    }
}