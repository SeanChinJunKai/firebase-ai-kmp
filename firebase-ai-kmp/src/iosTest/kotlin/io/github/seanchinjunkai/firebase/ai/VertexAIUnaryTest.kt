@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.CountTokensResponseObjc
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc
import io.github.seanchinjunkai.firebase.ai.type.BlockReason
import io.github.seanchinjunkai.firebase.ai.type.ContentModality
import io.github.seanchinjunkai.firebase.ai.type.FinishReason
import io.github.seanchinjunkai.firebase.ai.type.HarmCategory
import io.github.seanchinjunkai.firebase.ai.type.InvalidAPIKeyException
import io.github.seanchinjunkai.firebase.ai.type.PromptBlockedException
import io.github.seanchinjunkai.firebase.ai.type.QuotaExceededException
import io.github.seanchinjunkai.firebase.ai.type.ResponseStoppedException
import io.github.seanchinjunkai.firebase.ai.type.ServerException
import io.github.seanchinjunkai.firebase.ai.type.ServiceDisabledException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forAtLeastOne
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebaseAIBridge.GenerativeModelObjc as iOSGenerativeModel
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import platform.Foundation.NSError
import kotlin.experimental.ExperimentalNativeApi

class VertexAIUnaryTest {
    private val backend = "vertexai"

    @Test
    fun `short reply`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse(backend, "unary-success-basic-reply-short")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.generateContent("prompt")
        response.candidates.isEmpty() shouldBe false
        response.candidates.first().finishReason shouldBe FinishReason.STOP
        response.candidates.first().content.parts.isEmpty() shouldBe false
        response.candidates.first().safetyRatings.isEmpty() shouldBe false
    }

    @Test
    fun `long reply`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse(backend, "unary-success-basic-reply-long")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.generateContent("prompt")
        response.candidates.isEmpty() shouldBe false
        response.candidates.first().finishReason shouldBe FinishReason.STOP
        response.candidates.first().content.parts.isEmpty() shouldBe false
        response.candidates.first().safetyRatings.isEmpty() shouldBe false
    }

    @Test
    fun `response with detailed token-based usageMetadata`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse(backend, "unary-success-basic-response-long-usage-metadata")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.generateContent("prompt")
        response.candidates.isEmpty() shouldBe false
        response.candidates.first().finishReason shouldBe FinishReason.STOP
        response.candidates.first().content.parts.isEmpty() shouldBe false
        response.usageMetadata shouldNotBe null
        response.usageMetadata?.apply {
            totalTokenCount shouldBe 1913
            candidatesTokenCount shouldBe 76
            promptTokensDetails?.forAtLeastOne {
                it.modality shouldBe ContentModality.IMAGE
                it.tokenCount shouldBe 1806
            }
            candidatesTokensDetails?.forAtLeastOne {
                it.modality shouldBe ContentModality.TEXT
                it.tokenCount shouldBe 76
            }
        }
    }

    @Test
    fun `unknown enum in safety ratings`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val response = readGenerateContentResponse(backend, "unary-success-unknown-enum-safety-ratings")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.generateContent("prompt")

        response.candidates.isEmpty() shouldBe false
        val candidate = response.candidates.first()
        candidate.safetyRatings.any { it.category == HarmCategory.UNKNOWN } shouldBe true
        response.promptFeedback?.safetyRatings?.any { it.category == HarmCategory.UNKNOWN } shouldBe true
    }

    @Test
    fun `unknown enum in finish reason`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val error = readErrorResponse(backend, "unary-failure-unknown-enum-finish-reason")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        shouldThrow<ResponseStoppedException> { model.generateContent("prompt") } should
            {
                it.response?.candidates?.first()?.finishReason shouldBe FinishReason.UNKNOWN
            }
    }

    @Test
    fun `unknown enum in block reason`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val error = readErrorResponse(backend, "unary-failure-unknown-enum-prompt-blocked")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        shouldThrow<PromptBlockedException> { model.generateContent("prompt") } should
            {
                it.response?.promptFeedback?.blockReason shouldBe BlockReason.UNKNOWN
            }
    }

    @Test
    fun `http error`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val error = readErrorResponse(backend, "unary-failure-http-error")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        shouldThrow<ServerException> { model.generateContent("prompt") }
    }

    @Test
    fun `quota exceeded`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val error = readErrorResponse(backend, "unary-failure-quota-exceeded")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        shouldThrow<QuotaExceededException> { model.generateContent("prompt") }
    }

    @Test
    fun `invalid api key`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val error = readErrorResponse(backend, "unary-failure-api-key")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        shouldThrow<InvalidAPIKeyException> { model.generateContent("prompt") }
    }

    @Test
    fun `Vertex AI API disabled`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentWithContent(
                content: List<*>,
                completionHandler: (GenerateContentResponseObjc?, NSError?) -> Unit
            ) {
                val error = readErrorResponse(backend, "unary-failure-firebasevertexai-api-not-enabled")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        shouldThrow<ServiceDisabledException> { model.generateContent("prompt") }
    }

    @Test
    fun `countTokens fails with model not found`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithContent(
                content: List<*>,
                completionHandler: (CountTokensResponseObjc?, NSError?) -> Unit
            ) {
                val error = readErrorResponse(backend, "unary-failure-model-not-found")
                completionHandler(null, error)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        shouldThrow<ServerException> { model.countTokens("prompt") }
    }
}