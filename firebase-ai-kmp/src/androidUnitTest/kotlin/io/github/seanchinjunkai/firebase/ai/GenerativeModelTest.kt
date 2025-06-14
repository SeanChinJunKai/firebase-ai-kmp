package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import com.google.firebase.ai.type.CountTokensResponse as AndroidCountTokensResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class AndroidGenerativeModelTest {
    @Test
    fun `countTokens success and returns both totalTokens and totalBillableCharacters`() = runTest {
        val model = FakeAndroidGenerativeModel(FakeFirebaseAndroidGenerativeModel())
        val response = model.countTokens("test prompt")
        assert(response.totalTokens == 6)
        assert(response.totalBillableCharacters == 16)
    }

    @Test
    fun `countTokens success and returns only totalTokens`() = runTest {
        val fakeFirebaseAndroidModel = object : FakeFirebaseAndroidGenerativeModel() {
            override suspend fun countTokens(prompt: String): AndroidCountTokensResponse {
                return AndroidCountTokensResponse(
                    totalTokens = 258,
                )
            }
        }
        val model = FakeAndroidGenerativeModel(fakeFirebaseAndroidModel)
        val response = model.countTokens("test prompt")
        assert(response.totalTokens == 258)
        assert(response.totalBillableCharacters == null)
    }
}


class FakeAndroidGenerativeModel(val androidGenerativeModel: FakeFirebaseAndroidGenerativeModel): IGenerativeModel {
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
        return androidGenerativeModel.countTokens(prompt).toCountTokensResponse()
    }

    override suspend fun countTokens(vararg prompt: Content): CountTokensResponse {
        TODO("Not yet implemented")
    }
}

open class FakeFirebaseAndroidGenerativeModel() {
    open suspend fun countTokens(prompt: String): AndroidCountTokensResponse {
        return AndroidCountTokensResponse(
            totalTokens = 6,
            totalBillableCharacters = 16
        )
    }
}