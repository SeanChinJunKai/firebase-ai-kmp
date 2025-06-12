package io.github.seanchinjunkai.firebase.ai.type

public class CountTokensResponse(
    public val totalTokens: Int,
    public val totalBillableCharacters: Int? = null,
    public val promptTokensDetails: List<ModalityTokenCount> = emptyList()
) {
    public operator fun component1(): Int = totalTokens

    public operator fun component2(): Int? = totalBillableCharacters

    public operator fun component3(): List<ModalityTokenCount>? = promptTokensDetails
}