package pl.dn.booklist.data.remote

import io.reactivex.Single
import pl.dn.booklist.data.models.Book
import retrofit2.http.GET

interface ApiInterface {
    @GET("books.json")
    fun getBookList(): Single<List<Book>>
}