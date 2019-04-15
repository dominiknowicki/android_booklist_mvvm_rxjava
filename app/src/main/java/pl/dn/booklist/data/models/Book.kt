package pl.dn.booklist.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Book(
    @JsonProperty("author")
    val author: String,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("imageLink")
    val imageLink: String,
    @JsonProperty("language")
    val language: String,
    @JsonProperty("link")
    val link: String,
    @JsonProperty("pages")
    val pages: Int,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("year")
    val year: Int
)