package pl.dn.booklist.data

import io.reactivex.Single
import pl.dn.booklist.data.models.Book
import pl.dn.booklist.data.remote.ApiInterface

class DataModel(private val apiInterface: ApiInterface) {

    private var cachedBookList: List<Book>? = null

    fun getBookList(forceRefresh: Boolean): Single<List<Book>> {
        return if (forceRefresh || cachedBookList == null)
            apiInterface.getBookList()
                .flatMap {
                    cachedBookList = it.sortedBy { book -> book.title }
                    Single.just(cachedBookList)
                }
        else
            Single.just(cachedBookList)
    }
}