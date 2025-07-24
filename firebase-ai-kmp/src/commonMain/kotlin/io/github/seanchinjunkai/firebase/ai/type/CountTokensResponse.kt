package io.github.seanchinjunkai.firebase.ai.type

public class CountTokensResponse(
    public val totalTokens: Int,
    public val promptTokensDetails: List<ModalityTokenCount> = emptyList()
) {
    public operator fun component1(): Int = totalTokens

    public operator fun component2(): List<ModalityTokenCount>? = promptTokensDetails
}