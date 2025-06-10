package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.ContentModality
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.ModalityTokenCount
import io.github.seanchinjunkai.firebase.ai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class GenerativeModelTest {
    @Test
    fun countTokensTextInputSuccess() = runTest {
        val model = FakeGenerativeModel()
        val prompt = "Write a story about a magic backpack."
        val response = model.countTokens(prompt)

        assertEquals(response.totalTokens, prompt.length)
        assertEquals(response.promptTokensDetails.size, 1)
        assertEquals(response.promptTokensDetails[0].modality, ContentModality.TEXT)
        assertEquals(response.promptTokensDetails[0].tokenCount, prompt.length)
    }

    @Test
    fun countTokensMultimodalInputSuccess() = runTest {
        val model = FakeGenerativeModel()
        val textInput = "What is the contents of this video?"
        val prompt = content {
            fileData("MOCK_URL", "video/mp4")
            text(textInput)
        }
        val response = model.countTokens(prompt)

        assertTrue(response.totalTokens > 0)
        assertEquals(response.promptTokensDetails[0].modality, ContentModality.VIDEO)
        assertEquals(response.promptTokensDetails[0].tokenCount, 100)
        assertEquals(response.promptTokensDetails[1].modality, ContentModality.TEXT)
        assertEquals(response.promptTokensDetails[1].tokenCount, textInput.length)
    }
}

class FakeGenerativeModel: IGenerativeModel {
    override suspend fun generateContent(prompt: String): GenerateContentResponse {
        TODO("Not yet implemented")
    }

    override fun generateContentStream(prompt: String): Flow<GenerateContentResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun generateContent(vararg prompt: Content): GenerateContentResponse {
        TODO("Not yet implemented")
    }

    override fun generateContentStream(vararg prompt: Content): Flow<GenerateContentResponse> {
        TODO("Not yet implemented")
    }


    override suspend fun countTokens(prompt: String): CountTokensResponse {
        return CountTokensResponse(
            totalTokens = prompt.length,
            totalBillableCharacters = null,
            promptTokensDetails = listOf(
                ModalityTokenCount(
                    modality = ContentModality.TEXT,
                    tokenCount = prompt.length
                )
            )
        )
    }

    override suspend fun countTokens(vararg prompt: Content): CountTokensResponse {
        var totalTokens = 0

        val modalityTokenCounts = mutableMapOf<ContentModality, Int>()

        for (content in prompt) {
            for (part in content.parts) {
                val (modality, tokens) = when (part) {
                    is TextPart -> ContentModality.TEXT to part.text.length
                    is FileDataPart -> {
                        val modality = getModality(part.mimeType)
                        modality to 100
                    }
                    is ImagePart -> ContentModality.IMAGE to 200
                    is InlineDataPart -> {
                        val modality = getModality(part.mimeType)
                        modality to part.inlineData.size
                    }
                    else -> ContentModality.UNSPECIFIED to 0
                }

                totalTokens += tokens

                modalityTokenCounts[modality] = (modalityTokenCounts[modality] ?: 0) + tokens
            }
        }

        return CountTokensResponse(
            totalTokens = totalTokens,
            totalBillableCharacters = null,
            promptTokensDetails = modalityTokenCounts.map { (modality, count) ->
                ModalityTokenCount(
                    modality = modality,
                    tokenCount = count
                )
            }
        )
    }

    fun getModality(mimeType: String): ContentModality {
        return when {
            mimeType.startsWith("image/") -> ContentModality.IMAGE
            mimeType.startsWith("video/") -> ContentModality.VIDEO
            mimeType.startsWith("audio/") -> ContentModality.AUDIO
            mimeType.startsWith("application/") || mimeType.startsWith("text/") -> ContentModality.DOCUMENT
            else -> ContentModality.UNSPECIFIED
        }
    }
}
