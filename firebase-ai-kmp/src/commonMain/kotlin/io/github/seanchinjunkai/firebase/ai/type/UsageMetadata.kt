package io.github.seanchinjunkai.firebase.ai.type

public class UsageMetadata(
    public val promptTokenCount: Int,
    public val candidatesTokenCount: Int?,
    public val totalTokenCount: Int,
    public val promptTokensDetails: List<ModalityTokenCount>,
    public val candidatesTokensDetails: List<ModalityTokenCount>,
) {

}