package com.jeit.coconut.domain.models.brand

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BrandList(
    val code: Int?,
    val data: Data?,
    val message: String?
) : Parcelable {

    @Parcelize
    data class Data(
        val content: List<Content>?,
        val empty: Boolean?,
        val first: Boolean?,
        val last: Boolean?,
        val number: Int?,
        val numberOfElements: Int?,
        val pageable: Pageable?,
        val size: Int?,
        val sort: SortX?,
        val totalElements: Int?,
        val totalPages: Int?
    ) : Parcelable {

        @Parcelize
        data class Content(
            val brSeq: Int?,
            val brSubTitle: String?,
            val brEnName: String?,
            val brKrName: String?,
            val brLikeCount: Int?,
            val brImgSeq: Int?,
            val brCreateDatetime: String?,
            val imageUpload: String?
        ) : Parcelable


        @Parcelize
        data class Pageable(
            val offset: Int?,
            val pageNumber: Int?,
            val pageSize: Int?,
            val paged: Boolean?,
            val sort: Sort?,
            val unpaged: Boolean?
        ) : Parcelable {
            @Parcelize
            data class Sort(
                val empty: Boolean?,
                val sorted: Boolean?,
                val unsorted: Boolean?
            ) : Parcelable
        }

        @Parcelize
        data class SortX(
            val empty: Boolean?,
            val sorted: Boolean?,
            val unsorted: Boolean?
        ) : Parcelable
    }
}