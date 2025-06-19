package io.github.seanchinjunkai.firebase.ai.type

import kotlin.time.Duration.Companion.seconds

public class RequestOptions(
    internal val timeout: Long = 180.seconds.inWholeMilliseconds
) {

}
