package com.mindhub.speechtalk.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KakaoBook(
    val documents: List<Document>?,
    val meta: Meta?
) : Parcelable {

    @Parcelize
    data class Document(
        val authors: List<String>?,
        val contents: String?,
        val datetime: String?,
        val isbn: String?,
        val price: Int?,
        val publisher: String?,
        val sale_price: Int?,
        val status: String?,
        val thumbnail: String?,
        val title: String?,
        val translators: List<String>?,
        val url: String?,
        var like: Boolean
    ) : Parcelable

    @Parcelize
    data class Meta(
        val is_end: Boolean?,
        val pageable_count: Int?,
        val total_count: Int?
    ) : Parcelable
}
