package pl.dn.booklist

import android.support.multidex.MultiDexApplication
import android.widget.Toast
import io.reactivex.plugins.RxJavaPlugins
import pl.dn.booklist.data.DataModel
import pl.dn.booklist.data.remote.RetrofitImpl
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

const val APP_TAG = "DNBL_TAG"

class AppImpl : MultiDexApplication() {

    val dataModel: DataModel by lazy {
        DataModel(RetrofitImpl().apiInterface, RetrofitImpl().mediaApiInterface)
    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { e ->
            val errorMsg = when (e.cause) {
                is UnknownHostException, is SocketException, is SSLException -> getString(R.string.error_with_network_connection)
                else -> e.localizedMessage
            }
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }
}