package pl.dn.booklist.data.models.wikiresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class WikiResponse(
    @JsonProperty("batchcomplete")
    val batchcomplete: Boolean?,
    @JsonProperty("query")
    val query: WikiQuery?
) {
    fun getImageFileName(): String {
        var imageName = ""
        val wikiPage: WikiPage? = this.query?.let { query -> query.pages[0] }
        val filteredImageList = wikiPage?.images?.filter {
                wikiImage -> wikiImage.title.contains(".jpg")
                || wikiImage.title.contains(".jpeg")
                || wikiImage.title.contains(".png")
        }
        if (filteredImageList != null && filteredImageList.isNotEmpty())
            imageName = filteredImageList[0].title
        return imageName
    }

    fun getImageUrl(): String {
        var imageUrl = ""
        val wikiPage2: WikiPage? = this.query?.let { query -> query.pages[0] }
        wikiPage2?.imageinfo?.get(0)?.url?.let { url -> imageUrl = url }
        return imageUrl
    }
}