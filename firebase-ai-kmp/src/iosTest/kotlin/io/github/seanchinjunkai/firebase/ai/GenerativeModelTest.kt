@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcPromptBlocked
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcResponseStopped
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcServer
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc
import io.github.seanchinjunkai.firebase.ai.type.PromptBlockedException
import io.github.seanchinjunkai.firebase.ai.type.ResponseStoppedException
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

// Tests are based on vertexAI backend
class iOSGenerativeModelTest {
    @Test
    fun `countTokens succeeds`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithContent(
                content: List<*>,
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
            override fun countTokensWithContent(
                content: List<*>,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val response = readCountTokensResponse("unary-success-detailed-token-response")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("prompt")
        assert(response.totalTokens == 1837)
        assert(response.totalBillableCharacters == null)
        assertNotNull(response.promptTokensDetails)
        assertTrue {
            response.promptTokensDetails.any { it.modality.name == "IMAGE" && it.tokenCount == 1806 }
        }
    }

    @Test
    fun `countTokens fails with model not found`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithContent(
                content: List<*>,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {

                val error = readErrorResponse("unary-failure-model-not-found")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        assertFailsWith<ServerException> {
            model.countTokens("prompt")
        }
    }

    @Test
    fun `generateContent gives short reply`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse("unary-success-basic-reply-short")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.generateContent("prompt")
        assert(response.candidates.isNotEmpty())
        assert(response.candidates.first().finishReason?.name == "STOP")
        assert(response.candidates.first().content.parts.isNotEmpty())
    }

    @Test
    fun `generateContent gives long reply`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse("unary-success-basic-reply-long")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.generateContent("prompt")
        assert(response.candidates.isNotEmpty())
        assert(response.candidates.first().finishReason?.name == "STOP")
        assert(response.candidates.first().content.parts.isNotEmpty())
    }

    @Test
    fun `generateContent fails with unknown model`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val error = NSError(
                    domain = "FirebaseAIBridge",
                    code = FirebaseAIErrorObjcServer,
                    userInfo = mapOf(
                        "localizedDescription" to "models/unknown is not found for API version v1, or is not supported for GenerateContent. Call ListModels to see the list of available models and their supported methods."
                    )
                )
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        assertFailsWith<ServerException> {
            model.generateContent("prompt")
        }
    }

    @Test
    fun `generateContent fails with blocked prompt for safety with message`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse("unary-failure-prompt-blocked-safety-with-message")
                val error = NSError(
                    domain = "FirebaseAIBridge",
                    code = FirebaseAIErrorObjcPromptBlocked,
                    userInfo = mapOf(
                        "response" to response
                    )
                )
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val exception = assertFailsWith<PromptBlockedException> {
            model.generateContent("prompt")
        }
        assert(exception.response?.promptFeedback?.blockReason?.name == "SAFETY")
        assert(exception.response?.promptFeedback?.blockReasonMessage == "Reasons")
    }

    @Test
    fun `generateContent fails with response stopped for safety`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse("unary-failure-finish-reason-safety")
                val error = NSError(
                    domain = "FirebaseAIBridge",
                    code = FirebaseAIErrorObjcResponseStopped,
                    userInfo = mapOf(
                        "response" to response
                    )
                )
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val exception = assertFailsWith<ResponseStoppedException> {
            model.generateContent("prompt")
        }
        assert(exception.response?.candidates?.first()?.finishReason?.name == "SAFETY")
    }
}