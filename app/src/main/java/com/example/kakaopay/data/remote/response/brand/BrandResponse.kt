package com.example.kakaopay.data.remote.response.brand
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BrandResponse(
    @field:Json(name = "code") val code: Int?,
    @field:Json(name = "data") val data: List<Data>?,
    @field:Json(name = "message") val message: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @field:Json(name = "brSeq") val brSeq: Int?,
        @field:Json(name = "brSubTitle") val brSubTitle: String?,
        @field:Json(name = "brEnName") val brEnName: String?,
        @field:Json(name = "brKrName") val brKrName: String?,
        @field:Json(name = "brLikeCount") val brLikeCount: Int?,
        @field:Json(name = "brImgSeq") val brImgSeq: Int?,
        @field:Json(name = "brCreateDatetime") val brCreateDatetime: String?,
        @field:Json(name = "imageUpload") val imageUpload: String?
    )
}