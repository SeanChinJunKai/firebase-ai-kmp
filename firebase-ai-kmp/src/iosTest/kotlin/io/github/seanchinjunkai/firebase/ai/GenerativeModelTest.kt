@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcServer
import io.github.seanchinjunkai.firebase.ai.type.ServerException
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebaseAIBridge.CountTokensResponseObjc as iOSCountTokensResponse
import cocoapods.FirebaseAIBridge.GenerativeModelObjc as iOSGenerativeModel
import kotlinx.coroutines.test.runTest
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import platform.Foundation.NSError
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class iOSGenerativeModelTest {
    @Test
    fun `countTokens succeeds`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithPrompt(
                prompt: String,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val response = readCountTokensResponse("unary-success-total-tokens")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("prompt")
        assert(response.totalTokens == 6)
        assert(response.totalBillableCharacters == 16)
    }

    @Test
    fun `countTokens succeeds with no billable characters`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithPrompt(
                prompt: String,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val response = readCountTokensResponse("unary-success-no-billable-characters")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("prompt")
        assert(response.totalTokens == 258)
        assert(response.totalBillableCharacters == null)
    }

    @Test
    fun `countTokens with modality fields returned`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithPrompt(
                prompt: String,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val response = readCountTokensResponse("unary-success-detailed-token-response")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("prompt")
        assert(response.totalTokens == 1837)
        assert(response.totalBillableCharacters == 117)
        assertNotNull(response.promptTokensDetails)
        assertTrue {
            response.promptTokensDetails.any { it.modality.name == "IMAGE" && it.tokenCount == 1806 }
        }
    }

    @Test
    fun `countTokens fails with model not found`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithPrompt(
                prompt: String,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val error = NSError(
                    domain = "FirebaseAIBridge",
                    code = FirebaseAIErrorObjcServer,
                    userInfo = mapOf(
                        "localizedDescription" to "models/test-model-name is not found for API version v1beta, or is not supported for countTokens. Call ListModels to see the list of available models and their supported methods."
                    )
                )
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        assertFailsWith<ServerException> {
            model.countTokens("prompt")
        }
    }

}