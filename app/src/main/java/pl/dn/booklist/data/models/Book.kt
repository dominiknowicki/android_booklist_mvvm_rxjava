package pl.dn.booklist.data.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    @JsonProperty("author")
    val author: String?,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("imageLink")
    val imageLink: String?,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("year")
    val year: Int
) : Parcelable