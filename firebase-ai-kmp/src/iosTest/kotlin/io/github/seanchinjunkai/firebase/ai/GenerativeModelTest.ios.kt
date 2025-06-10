@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)


package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebaseAIBridge.CountTokensResponseObjc as iOSCountTokensResponse
import cocoapods.FirebaseAIBridge.ModalityTokenCountObjc as iOSModalityTokenCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.runTest
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import platform.Foundation.NSNumber
import kotlin.coroutines.resume

class iOSGenerativeModelTest {
    @Test
    fun `countTokens success and returns both totalTokens and totalBillableCharacters`() = runTest {
        val model = FakeiOSGenerativeModel(FakeFirebaseiOSGenerativeModel())
        val response = model.countTokens("test prompt")
        assert(response.totalTokens == 6)
        assert(response.totalBillableCharacters == 16)
    }

    @Test
    fun `countTokens success and returns only totalTokens`() = runTest {
        val fakeFirebaseiOSModel = object : FakeFirebaseiOSGenerativeModel() {
            override fun countTokens(prompt: String): iOSCountTokensResponse {
                return iOSCountTokensResponse(
                    totalTokens = 258,
                    totalBillableCharacters = null,
                    promptTokenDetails = emptyList<iOSModalityTokenCount>()
                )
            }
        }
        val model = FakeiOSGenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("test prompt")
        assert(response.totalTokens == 258)
        assert(response.totalBillableCharacters == null)
    }
}


class FakeiOSGenerativeModel(val iosGenerativeModel: FakeFirebaseiOSGenerativeModel): IGenerativeModel {
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

    override suspend fun countTokens(prompt: String): CountTokensResponse =
        suspendCancellableCoroutine { continuation ->
            val result = iosGenerativeModel.countTokens(prompt)
            continuation.resume(result.toCountTokensResponse())
        }

    override suspend fun countTokens(vararg prompt: Content): CountTokensResponse {
        TODO("Not yet implemented")
    }
}


open class FakeFirebaseiOSGenerativeModel() {
    open fun countTokens(prompt: String): iOSCountTokensResponse {
        return iOSCountTokensResponse(
            totalTokens = 6,
            totalBillableCharacters = NSNumber(16),
            promptTokenDetails = emptyList<iOSModalityTokenCount>()
        )
    }
}