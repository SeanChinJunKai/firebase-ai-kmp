package io.github.seanchinjunkai.firebase.ai.type

public class FunctionCallingConfig
internal constructor(
    internal val mode: Mode,
    internal val allowedFunctionNames: List<String>? = null
) {

    /** Configuration for dictating when the model should call the attached function. */
    internal enum class Mode {
        AUTO,
        ANY,
        NONE,
    }

    public companion object {
        public fun auto(): FunctionCallingConfig = FunctionCallingConfig(Mode.AUTO)

        public fun any(allowedFunctionNames: List<String>? = null): FunctionCallingConfig =
            FunctionCallingConfig(Mode.ANY, allowedFunctionNames)

        public fun none():FunctionCallingConfig = FunctionCallingConfig(Mode.NONE)
    }
}
