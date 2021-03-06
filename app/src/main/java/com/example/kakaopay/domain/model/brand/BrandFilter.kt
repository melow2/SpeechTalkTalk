package com.jeit.coconut.domain.models.brand

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrandFilter(
    val filters: List<Data>?
) : Parcelable {
    @Parcelize
    data class Data(
        val fcName: String?,
        val id: Int?,
        val item: List<Item>?
    ) : Parcelable {
        @Parcelize
        data class Item(
            val fciActivationImgPath: String?,
            val fciDeactivationImgPath: String?,
            val fciName: String?,
            val id: Int?,
            var selected:Boolean
        ) : Parcelable
    }
}