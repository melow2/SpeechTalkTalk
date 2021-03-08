package com.khs.kakaopay.data.remote.response.book

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class KakaoBookResponse(
    val documents: List<Document>?,
    val meta: Meta?
) {

    @JsonClass(generateAdapter = true)
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
        val url: String?
    )

    @JsonClass(generateAdapter = true)
    data class Meta(
        val is_end: Boolean?,
        val pageable_count: Int?,
        val total_count: Int?
    )
}
