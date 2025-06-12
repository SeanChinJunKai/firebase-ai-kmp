package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GenerativeModelTest {
    @Test
    fun `countTokens success and returns both totalTokens and totalBillableCharacters`() = runTest {
        val model = FakeGenerativeModel()
        val response = model.countTokens("test prompt")
        assertTrue(response.totalTokens == 6)
        assertTrue(response.totalBillableCharacters == 16)
    }

    @Test
    fun `countTokens success and returns only totalTokens`() = runTest {
        val model = object : FakeGenerativeModel() {
            override suspend fun countTokens(prompt: String): CountTokensResponse {
                return CountTokensResponse(
                    totalTokens = 258
                )
            }
        }
        val response = model.countTokens("test prompt")
        assertTrue(response.totalTokens == 258)
        assertTrue(response.totalBillableCharacters == null)
    }
}


open class FakeGenerativeModel: IGenerativeModel {
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
            totalTokens = 6,
            totalBillableCharacters = 16
        )
    }

    override suspend fun countTokens(vararg prompt: Content): CountTokensResponse {
        TODO("Not yet implemented")
    }
}
