package com.example.kakaopay.data.remote.response.brand

import com.jeit.coconut.data.remote.response.brand.BrandListResponse
import com.jeit.coconut.domain.models.brand.Brand
import com.jeit.coconut.domain.models.brand.BrandDetail
import com.jeit.coconut.domain.models.brand.BrandFilter
import com.jeit.coconut.domain.models.brand.BrandList

object BrandMapper {
    fun responseToBrandModel(response: BrandResponse): Brand {
        return Brand(
            code = response.code,
            data = response.data?.map {
                Brand.Data(
                    brEnName = it.brEnName,
                    brImgSeq = it.brImgSeq,
                    brKrName = it.brKrName,
                    brLikeCount = it.brLikeCount,
                    brSeq = it.brSeq,
                    brSubTitle = it.brSubTitle,
                    imageUpload = it.imageUpload
                )
            },
            message = response.message
        )
    }

    fun responseToBrandListModel(response: BrandListResponse): BrandList {
        return BrandList(
            code = response.code,
            data = response.data.let { data ->
                BrandList.Data(
                    content = data?.content?.map {
                        BrandList.Data.Content(
                            brEnName = it.brEnName,
                            brImgSeq = it.brImgSeq,
                            brKrName = it.brKrName,
                            brLikeCount = it.brLikeCount,
                            brSeq = it.brSeq,
                            brSubTitle = it.brSubTitle,
                            imageUpload = it.imageUpload,
                            brCreateDatetime = it.brCreateDatetime
                        )
                    },
                    empty = data?.empty,
                    first = data?.first,
                    last = data?.last,
                    number = data?.number,
                    numberOfElements = data?.numberOfElements,
                    pageable = data?.pageable.let {
                        BrandList.Data.Pageable(
                            offset = it?.offset,
                            pageNumber = it?.pageNumber,
                            pageSize = it?.pageSize,
                            paged = it?.paged,
                            sort = it?.sort.let { it ->
                                BrandList.Data.Pageable.Sort(
                                    empty = it?.empty,
                                    sorted = it?.sorted,
                                    unsorted = it?.unsorted
                                )
                            },
                            unpaged = it?.unpaged
                        )
                    },
                    size = data?.size,
                    sort = data?.sort.let {
                        BrandList.Data.SortX(
                            empty = it?.empty,
                            sorted = it?.sorted,
                            unsorted = it?.unsorted
                        )
                    },
                    totalElements = data?.totalElements,
                    totalPages = data?.totalPages
                )
            },
            message = response.message
        )
    }

    fun responseToBrandDetailModel(response: BrandDetailResponse): BrandDetail {
        return BrandDetail(
            brContentURL = response.data.brContentURL,
            brCreateTime = response.data.brCreateTime,
            brEnName = response.data.brEnName,
            brKrName = response.data.brKrName,
            brLikeCount = response.data.brLikeCount,
            brSeq = response.data.brSeq,
            categoryItems = response.data.categoryItems?.map {
                BrandDetail.CategoryItem(
                    fciActivationImgPath = it.fciActivationImgPath,
                    fciDeactivationImgPath = it.fciDeactivationImgPath,
                    fciName = it.fciName,
                    id = it.id
                )
            },
            likeStatus = response.data.likeStatus,
            wishStatus = response.data.wishStatus
        )
    }

    fun responseToBrandFilterModel(response: BrandFilterResponse): BrandFilter {
        return BrandFilter(
            filters = response.data?.map {
                BrandFilter.Data(
                    fcName = it.fcName,
                    id = it.id,
                    item = it.item?.map { item ->
                        BrandFilter.Data.Item(
                            fciActivationImgPath = item.fciActivationImgPath,
                            fciDeactivationImgPath = item.fciDeactivationImgPath,
                            fciName = item.fciName,
                            id = item.id,
                            selected = false
                        )
                    }
                )
            }
        )
    }
}




