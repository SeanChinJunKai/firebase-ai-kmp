package io.github.seanchinjunkai.firebase.ai.type

public class ModalityTokenCount(
    public val modality: ContentModality,
    public val tokenCount: Int
) {
    public operator fun component1(): ContentModality = modality

    public operator fun component2(): Int = tokenCount
}