@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebaseAIBridge.CountTokensResponseObjc as iOSCountTokensResponse
import cocoapods.FirebaseAIBridge.ModalityTokenCountObjc as iOSModalityTokenCount
import cocoapods.FirebaseAIBridge.GenerativeModelObjc as iOSGenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.runTest
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import kotlin.coroutines.resume
import platform.Foundation.NSNumber
import platform.Foundation.NSError

class iOSGenerativeModelTest {
    @Test
    fun `countTokens success and returns both totalTokens and totalBillableCharacters`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithPrompt(
                prompt: String,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val response = iOSCountTokensResponse(
                    totalTokens = 6,
                    totalBillableCharacters = NSNumber(16),
                    promptTokenDetails = emptyList<iOSModalityTokenCount>()
                )
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("test prompt")
        assert(response.totalTokens == 6)
        assert(response.totalBillableCharacters == 16)
    }

    @Test
    fun `countTokens success and returns only totalTokens`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithPrompt(
                prompt: String,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val response = iOSCountTokensResponse(
                    totalTokens = 258,
                    totalBillableCharacters = null,
                    promptTokenDetails = emptyList<iOSModalityTokenCount>()
                )
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("test prompt")
        assert(response.totalTokens == 258)
        assert(response.totalBillableCharacters == null)
    }
}


