package pl.dn.booklist.data.models.wikiresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class WikiThumbnail(
    @JsonProperty("source")
    val source: String?
)