package io.github.seanchinjunkai.firebase.ai.type

public class SafetySetting(
    internal val harmCategory: HarmCategory,
    internal val threshold: HarmBlockThreshold,
    internal val method: HarmBlockMethod? = null,
) {

}