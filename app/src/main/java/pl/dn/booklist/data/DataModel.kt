package pl.dn.booklist.data

import io.reactivex.Single
import pl.dn.booklist.data.models.Book
import pl.dn.booklist.data.models.wikiresponse.WikiResponse
import pl.dn.booklist.data.remote.ApiInterface
import pl.dn.booklist.data.remote.MediaApiInterface

class DataModel(private val apiInterface: ApiInterface, private val mediaApiInterface: MediaApiInterface) {

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

    fun getImageFileName(query: String): Single<WikiResponse> {
        return mediaApiInterface.getImageFileName(getImageNameParams(query))
    }

    fun getImageFileUrl(query: String): Single<WikiResponse> {
        return mediaApiInterface.getImageFileUrl(getImageFileUrlParams(query))
    }

    // eg https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&prop=images&titles=Children_of_Gebelawi
    private fun getImageNameParams(query: String): Map<String, String> {
        return mapOf(
            "action" to "query",
            "format" to "json",
            "formatversion" to "2",
            "prop" to "images",
            "titles" to query
        )
    }

    // eg https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&titles=File:Children%20of%20Gebelawi.jpg&prop=imageinfo&iiprop=url
    private fun getImageFileUrlParams(query: String): Map<String, String> {
        return mapOf(
            "action" to "query",
            "format" to "json",
            "formatversion" to "2",
            "prop" to "imageinfo",
            "iiprop" to "url",
            "titles" to query
        )
    }
}