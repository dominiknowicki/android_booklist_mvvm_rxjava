package pl.dn.booklist.data.models.wikiresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class WikiQuery(
    @JsonProperty("pages")
    val pages: List<WikiPage>
)