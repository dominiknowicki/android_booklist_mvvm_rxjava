package pl.dn.booklist.data.models.wikiresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class WikiImageInfo(
    @JsonProperty("url")
    val url: String?,
    @JsonProperty("descriptionshorturl")
    val descriptionshorturl: String?
)