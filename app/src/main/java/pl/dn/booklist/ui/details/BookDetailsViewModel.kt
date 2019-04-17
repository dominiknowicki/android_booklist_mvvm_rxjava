package pl.dn.booklist.ui.details

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.dn.booklist.data.DataModel

class BookDetailsViewModel() : ViewModel() {

    private lateinit var mDataModel: DataModel

    constructor(dataModel: DataModel) : this() {
        mDataModel = dataModel
    }

    @SuppressLint("CheckResult")
    fun getImageUrl(query: String): Single<String> {
        return mDataModel.getImageFileName(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { wikiResponse1 -> getImageFileUrl(wikiResponse1.getImageFileName()) }
    }

    private fun getImageFileUrl(imageName: String): Single<String> {
        return mDataModel.getImageFileUrl(imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { wikiResponse2 -> Single.just(wikiResponse2.getImageUrl()) }
    }
}