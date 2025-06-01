@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.ContentModality
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.ModalityTokenCount
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebaseAIBridge.CountTokensResponseObjc as iOSCountTokensResponse
import cocoapods.FirebaseAIBridge.ModalityTokenCountObjc as iOSModalityTokenCount
import cocoapods.FirebaseAIBridge.ContentModalityObjc as iOSContentModality

public fun iOSCountTokensResponse.toCountTokensResponse(): CountTokensResponse {
    return CountTokensResponse(
        totalTokens = this.totalTokens().toInt(),
        totalBillableCharacters = this.totalBillableCharacters()?.intValue(),
        promptTokensDetails = this.promptTokenDetails().map {
            val tokenCount = it as iOSModalityTokenCount
            tokenCount.toModalityTokenCount()
        }
    )
}

public fun iOSModalityTokenCount.toModalityTokenCount(): ModalityTokenCount {
    return ModalityTokenCount(
        modality = this.modality().toContentModality(),
        tokenCount = this.tokenCount().toInt()
    )
}

public fun iOSContentModality.toContentModality(): ContentModality {
    return ContentModality(
        ordinal = this.toInt()
    )
}