package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.ContentModality
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.ModalityTokenCount
import com.google.firebase.ai.type.CountTokensResponse as AndroidCountTokensResponse
import com.google.firebase.ai.type.ModalityTokenCount as AndroidModalityTokenCount
import com.google.firebase.ai.type.ContentModality as AndroidContentModality

public fun AndroidCountTokensResponse.toCountTokensResponse(): CountTokensResponse {
    return CountTokensResponse(
        totalTokens = this.totalTokens,
        totalBillableCharacters = this.totalBillableCharacters,
        promptTokensDetails = this.promptTokensDetails.map { it.toModalityTokenCount() }
    )
}

public fun AndroidModalityTokenCount.toModalityTokenCount(): ModalityTokenCount {
    return ModalityTokenCount(
        modality = this.modality.toContentModality(),
        tokenCount = this.tokenCount
    )
}

public fun AndroidContentModality.toContentModality(): ContentModality {
    return ContentModality(
        ordinal = this.ordinal
    )
}