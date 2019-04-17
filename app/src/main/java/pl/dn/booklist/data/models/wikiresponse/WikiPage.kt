package pl.dn.booklist.data.models.wikiresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class WikiPage(
    @JsonProperty("missing")
    val missing: Boolean?,
    @JsonProperty("thumbnail")
    val thumbnail: WikiThumbnail?,
    @JsonProperty("images")
    val images: List<WikiImage>?,
    @JsonProperty("imageinfo")
    val imageinfo: List<WikiImageInfo>?
)