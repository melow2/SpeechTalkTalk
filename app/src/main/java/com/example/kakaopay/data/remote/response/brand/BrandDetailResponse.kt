package com.example.kakaopay.data.remote.response.brand

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class BrandDetailResponse(
    @field:Json(name = "code") val code: Int?,
    @field:Json(name = "data") val data: Data,
    @field:Json(name = "message") val message: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @field:Json(name = "brContentURL") val brContentURL: String?,
        @field:Json(name = "brCreateTime") val brCreateTime: String?,
        @field:Json(name = "brEnName") val brEnName: String?,
        @field:Json(name = "brKrName") val brKrName: String?,
        @field:Json(name = "brLikeCount") val brLikeCount: Int?,
        @field:Json(name = "brSeq") val brSeq: Int?,
        @field:Json(name = "categoryItems") val categoryItems: List<CategoryItem>?,
        @field:Json(name = "likeStatus") val likeStatus: Boolean?,
        @field:Json(name = "wishStatus") val wishStatus: Boolean?
    ) {
        @JsonClass(generateAdapter = true)
        data class CategoryItem(
            @field:Json(name = "fciActivationImgPath") val fciActivationImgPath: String?,
            @field:Json(name = "fciDeactivationImgPath") val fciDeactivationImgPath: String?,
            @field:Json(name = "fciName") val fciName: String?,
            @field:Json(name = "id") val id: Int?
        )
    }
}