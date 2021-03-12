package com.khs.kakaopay.domain

import com.attractive.deer.util.data.Either

typealias KakaoPayResult<T> = Either<KakaoPayAppError, T>