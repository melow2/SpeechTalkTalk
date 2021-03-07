package com.khs.kakaopay.data.remote.response.brand

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BrandListResponse(
    @field:Json(name = "code") val code: Int?,
    @field:Json(name = "data") val data: Data?,
    @field:Json(name = "message") val message: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @field:Json(name = "content")val content: List<Content>?,
        @field:Json(name = "empty")val empty: Boolean?,
        @field:Json(name = "first")val first: Boolean?,
        @field:Json(name = "last")val last: Boolean?,
        @field:Json(name = "number") val number: Int?,
        @field:Json(name = "numberOfElements")val numberOfElements: Int?,
        @field:Json(name = "pageable")val pageable: Pageable?,
        @field:Json(name = "size")val size: Int?,
        @field:Json(name = "sort")val sort: SortX?,
        @field:Json(name = "totalElements")val totalElements: Int?,
        @field:Json(name = "totalPages")val totalPages: Int?
    ) {
        @JsonClass(generateAdapter = true)
        data class Content(
            @field:Json(name = "brSeq") val brSeq: Int?,
            @field:Json(name = "brSubTitle") val brSubTitle: String?,
            @field:Json(name = "brEnName") val brEnName: String?,
            @field:Json(name = "brKrName") val brKrName: String?,
            @field:Json(name = "brLikeCount") val brLikeCount: Int?,
            @field:Json(name = "brImgSeq") val brImgSeq: Int?,
            @field:Json(name = "brCreateDatetime") val brCreateDatetime: String?,
            @field:Json(name = "imageUpload") val imageUpload: String?
        )

        @JsonClass(generateAdapter = true)
        data class Pageable(
            @field:Json(name = "offset") val offset: Int?,
            @field:Json(name = "pageNumber") val pageNumber: Int?,
            @field:Json(name = "pageSize") val pageSize: Int?,
            @field:Json(name = "paged") val paged: Boolean?,
            @field:Json(name = "sort") val sort: Sort?,
            @field:Json(name = "unpaged") val unpaged: Boolean?
        ) {
            @JsonClass(generateAdapter = true)
            data class Sort(
                @field:Json(name = "empty") val empty: Boolean?,
                @field:Json(name = "sorted") val sorted: Boolean?,
                @field:Json(name = "unsorted") val unsorted: Boolean?
            )
        }

        @JsonClass(generateAdapter = true)
        data class SortX(
            @field:Json(name = "empty") val empty: Boolean?,
            @field:Json(name = "sorted") val sorted: Boolean?,
            @field:Json(name = "unsorted") val unsorted: Boolean?
        )
    }
}