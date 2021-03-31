package com.mindhub.speechtalk.domain

import com.attractive.deer.util.data.Either

typealias SpeechTalkResult<T> = Either<SpeechTalkAppError, T>