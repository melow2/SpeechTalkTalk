package com.jeit.coconut.domain.models.brand

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(
    val code: Int?,
    val data: List<Data>?,
    val message: String?
) : Parcelable {
    @Parcelize
    data class Data(
        val brEnName: String?,
        val brImgSeq: Int?,
        val brKrName: String?,
        val brLikeCount: Int?,
        val brSeq: Int?,
        val brSubTitle: String?,
        val imageUpload: String?
    ) : Parcelable
}