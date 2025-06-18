package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.InvalidStateException
import io.github.seanchinjunkai.firebase.ai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Semaphore

public class Chat(
    private val model: IGenerativeModel,
    public val history: MutableList<Content> = ArrayList()
) {
    private var lock = Semaphore(1)

    public suspend fun sendMessage(prompt: Content): GenerateContentResponse {
        prompt.assertComesFromUser()
        attemptLock()
        try {
            val response = model.generateContent(*history.toTypedArray(), prompt)
            history.add(prompt)
            history.add(response.candidates.first().content)
            return response
        } finally {
            lock.release()
        }
    }

    public suspend fun sendMessage(prompt: String): GenerateContentResponse {
        val content = content { text(prompt) }
        return sendMessage(content)
    }

    // TODO: https://github.com/firebase/firebase-android-sdk/blob/main/firebase-ai/src/main/kotlin/com/google/firebase/ai/Chat.kt#L135
    public fun sendMessageStream(prompt: Content): Flow<GenerateContentResponse> {
        val flow = model.generateContentStream(*history.toTypedArray(), prompt)
        val images = mutableListOf<PlatformImage>()
        val inlineDataParts = mutableListOf<InlineDataPart>()
        val text = StringBuilder()

        return flow
            .onEach {
                for (part in it.candidates.first().content.parts) {
                    when (part) {
                        is TextPart -> text.append(part.text)
                        is ImagePart -> images.add(part.image)
                        is InlineDataPart -> inlineDataParts.add(part)
                    }
                }
            }
            .onCompletion {
                lock.release()
                if (it == null) {
                    val content =
                        content("model") {
                            for (image in images) {
                                image(image)
                            }
                            for (inlineDataPart in inlineDataParts) {
                                inlineData(inlineDataPart.inlineData, inlineDataPart.mimeType)
                            }
                            if (text.isNotBlank()) {
                                text(text.toString())
                            }
                        }

                    history.add(prompt)
                    history.add(content)
                }
            }

    }

    public fun sendMessageStream(prompt: String) : Flow<GenerateContentResponse> {
        val content = content { text(prompt) }
        return sendMessageStream(content)
    }

    private fun Content.assertComesFromUser() {
        if (role !in listOf("user", "function")) {
            throw InvalidStateException("Chat prompts should come from the 'user' or 'function' role.")
        }
    }

    private fun attemptLock() {
        if (!lock.tryAcquire()) {
            throw InvalidStateException(
                "This chat instance currently has an ongoing request, please wait for it to complete " +
                        "before sending more messages"
            )
        }
    }
}