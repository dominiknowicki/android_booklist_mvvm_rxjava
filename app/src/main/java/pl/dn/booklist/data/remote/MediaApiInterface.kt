package pl.dn.booklist.data.remote

import io.reactivex.Single
import pl.dn.booklist.data.models.wikiresponse.WikiResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MediaApiInterface {

    // eg https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&prop=images&titles=Children_of_Gebelawi
    @GET("w/api.php")
    fun getImageFileName(@QueryMap params: Map<String, String>): Single<WikiResponse>

    // eg https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&titles=File:Children%20of%20Gebelawi.jpg&prop=imageinfo&iiprop=url
    @GET("w/api.php")
    fun getImageFileUrl(@QueryMap params: Map<String, String>): Single<WikiResponse>
}