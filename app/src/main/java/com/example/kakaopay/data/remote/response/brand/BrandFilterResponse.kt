package com.example.kakaopay.data.remote.response.brand
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BrandFilterResponse(
    @field:Json(name = "code") val code: Int?,
    @field:Json(name = "data") val data: List<Data>?,
    @field:Json(name = "message") val message: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @field:Json(name = "fcName") val fcName: String?,
        @field:Json(name = "id") val id: Int?,
        @field:Json(name = "item") val item: List<Item>?
    ) {
        @JsonClass(generateAdapter = true)
        data class Item(
            @field:Json(name = "fciActivationImgPath") val fciActivationImgPath: String?,
            @field:Json(name = "fciDeactivationImgPath") val fciDeactivationImgPath: String?,
            @field:Json(name = "fciName") val fciName: String?,
            @field:Json(name = "id") val id: Int?
        )
    }
}