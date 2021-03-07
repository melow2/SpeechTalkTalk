package com.khs.kakaopay.data.remote.response.brand

import com.khs.kakaopay.domain.model.brand.Brand
import com.khs.kakaopay.domain.model.brand.BrandDetail
import com.khs.kakaopay.domain.model.brand.BrandFilter
import com.khs.kakaopay.domain.model.brand.BrandList


object BrandMapper {
    fun responseToBrandModel(response: BrandResponse): Brand {
        return Brand(
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
        )
    }

    fun responseToBrandListModel(response: BrandListResponse): BrandList {
        return BrandList(
            content = response.data?.content?.map {
                BrandList.Content(
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
            empty = response.data?.empty,
            first = response.data?.first,
            last = response.data?.last,
            number = response.data?.number,
            numberOfElements = response.data?.numberOfElements,
            pageable = response.data?.pageable.let {
                BrandList.Pageable(
                    offset = it?.offset,
                    pageNumber = it?.pageNumber,
                    pageSize = it?.pageSize,
                    paged = it?.paged,
                    sort = it?.sort.let { it ->
                        BrandList.Pageable.Sort(
                            empty = it?.empty,
                            sorted = it?.sorted,
                            unsorted = it?.unsorted
                        )
                    },
                    unpaged = it?.unpaged
                )
            },
            size = response.data?.size,
            sort = response.data?.sort.let {
                BrandList.SortX(
                    empty = it?.empty,
                    sorted = it?.sorted,
                    unsorted = it?.unsorted
                )
            },
            totalElements = response.data?.totalElements,
            totalPages = response.data?.totalPages
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




