package pl.dn.booklist.data

import io.reactivex.Single
import pl.dn.booklist.data.models.Book
import pl.dn.booklist.data.remote.ApiInterface

class DataModel(private val apiInterface: ApiInterface) {

    private var cachedBookList: ArrayList<Book>? = null

    fun getBookList(forceRefresh: Boolean): Single<ArrayList<Book>> {
        return if (forceRefresh || cachedBookList == null)
            apiInterface.getBookList()
                .doOnSuccess { cachedBookList = it }
        else
            Single.just(cachedBookList)
    }
}