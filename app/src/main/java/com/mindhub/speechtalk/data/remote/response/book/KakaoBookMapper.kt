package com.mindhub.speechtalk.data.remote.response.book

import com.mindhub.speechtalk.domain.model.KakaoBook


object KakaoBookMapper {
    fun responseToKakaoBookListModel(response: KakaoBookResponse): KakaoBook {
        return KakaoBook(
            documents = response.documents?.map {
                KakaoBook.Document(
                    authors = it.authors,
                    contents = it.contents,
                    datetime = it.datetime,
                    isbn = it.isbn,
                    price = it.price,
                    publisher = it.publisher,
                    sale_price = it.sale_price,
                    status = it.status,
                    thumbnail = it.thumbnail,
                    title = it.title,
                    translators = it.translators,
                    url = it.url,
                    like = false
                )
            },
            meta = KakaoBook.Meta(
                is_end = response.meta?.is_end,
                pageable_count = response.meta?.pageable_count,
                total_count = response.meta?.total_count
            )
        )
    }

}




