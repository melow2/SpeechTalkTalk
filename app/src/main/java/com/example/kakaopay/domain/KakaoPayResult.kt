package com.example.kakaopay.domain

import com.lovely.deer.util.data.Either

typealias KakaoPayResult<T> = Either<KakaoPayAppError, T>