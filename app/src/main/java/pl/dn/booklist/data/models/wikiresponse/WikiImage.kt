package pl.dn.booklist.data.models.wikiresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class WikiImage(
    @JsonProperty("title")
    val title: String
)