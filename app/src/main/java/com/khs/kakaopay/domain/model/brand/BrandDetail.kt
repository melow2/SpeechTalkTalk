package com.khs.kakaopay.domain.model.brand;

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BrandDetail(
    val brContentURL: String?,
    val brCreateTime: String?,
    val brEnName: String?,
    val brKrName: String?,
    val brLikeCount: Int?,
    val brSeq: Int?,
    val categoryItems: List<CategoryItem>?,
    val likeStatus: Boolean?,
    val wishStatus: Boolean?
) : Parcelable {
    @Parcelize
    data class CategoryItem(
        val fciActivationImgPath: String?,
        val fciDeactivationImgPath: String?,
        val fciName: String?,
        val id: Int?
    ) : Parcelable
}