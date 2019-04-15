package pl.dn.booklist

import android.support.multidex.MultiDexApplication
import pl.dn.booklist.data.DataModel
import pl.dn.booklist.data.remote.ApiInterface
import pl.dn.booklist.data.remote.RetrofitImpl

class AppImpl : MultiDexApplication() {
    private var apiInterface: ApiInterface = RetrofitImpl().apiInterface
    lateinit var dataModel: DataModel

    override fun onCreate() {
        super.onCreate()
        dataModel = DataModel(apiInterface)
    }
}