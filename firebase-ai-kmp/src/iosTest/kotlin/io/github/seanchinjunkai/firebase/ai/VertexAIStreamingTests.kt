@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc
import io.github.seanchinjunkai.firebase.ai.type.BlockReason
import io.github.seanchinjunkai.firebase.ai.type.FinishReason
import io.github.seanchinjunkai.firebase.ai.type.HarmCategory
import io.github.seanchinjunkai.firebase.ai.type.InvalidAPIKeyException
import io.github.seanchinjunkai.firebase.ai.type.PromptBlockedException
import io.github.seanchinjunkai.firebase.ai.type.ResponseStoppedException
import io.github.seanchinjunkai.firebase.ai.type.ServerException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import cocoapods.FirebaseAIBridge.GenerativeModelObjc as iOSGenerativeModel
import kotlinx.coroutines.test.runTest
import platform.Foundation.NSError
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

class VertexAIStreamingTests {
    private val backend = "vertexai"

    @Test
    fun `short reply`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentStreamWithContent(
                content: List<*>,
                onResponse: (GenerateContentResponseObjc?) -> Unit,
                onComplete: (NSError?) -> Unit
            ) {
                readStreamingResponse(backend, "streaming-success-basic-reply-short", onResponse, onComplete)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val responses = model.generateContentStream("prompt")
        val responseList = responses.toList()
        responseList.isEmpty() shouldBe false
        responseList.last().candidates.first().apply {
            finishReason shouldBe FinishReason.STOP
            content.parts.isEmpty() shouldBe false
            safetyRatings.isEmpty() shouldBe false
        }
    }

    @Test
    fun `long reply`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentStreamWithContent(
                content: List<*>,
                onResponse: (GenerateContentResponseObjc?) -> Unit,
                onComplete: (NSError?) -> Unit
            ) {
                readStreamingResponse(backend, "streaming-success-basic-reply-long", onResponse, onComplete)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val responses = model.generateContentStream("prompt")
        val responseList = responses.toList()
        responseList.isEmpty() shouldBe false
        responseList.last().candidates.first().apply {
            finishReason shouldBe FinishReason.STOP
            content.parts.isEmpty() shouldBe false
        }
    }

    @Test
    fun `unknown enum in safety ratings`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentStreamWithContent(
                content: List<*>,
                onResponse: (GenerateContentResponseObjc?) -> Unit,
                onComplete: (NSError?) -> Unit
            ) {
                readStreamingResponse(backend, "streaming-success-unknown-safety-enum", onResponse, onComplete)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val responses = model.generateContentStream("prompt")
        val responseList = responses.toList()
        responseList.isEmpty() shouldBe false
        responseList.any {
            it.candidates.any { it.safetyRatings.any { it.category == HarmCategory.UNKNOWN } }
        } shouldBe true
    }

    @Test
    fun `unknown enum in finish reason`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentStreamWithContent(
                content: List<*>,
                onResponse: (GenerateContentResponseObjc?) -> Unit,
                onComplete: (NSError?) -> Unit
            ) {
                readStreamingResponse(backend, "streaming-failure-unknown-finish-enum", onResponse, onComplete)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val responses = model.generateContentStream("prompt")
        val exception = shouldThrow<ResponseStoppedException> { responses.collect() }
        exception.response?.candidates?.first()?.finishReason shouldBe FinishReason.UNKNOWN
    }

    @Test
    fun `prompt blocked for safety`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentStreamWithContent(
                content: List<*>,
                onResponse: (GenerateContentResponseObjc?) -> Unit,
                onComplete: (NSError?) -> Unit
            ) {
                readStreamingResponse(backend, "streaming-failure-prompt-blocked-safety-with-message", onResponse, onComplete)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val responses = model.generateContentStream("prompt")
        val exception = shouldThrow<PromptBlockedException> { responses.collect() }
        exception.response?.promptFeedback?.blockReason shouldBe BlockReason.SAFETY
        exception.response?.promptFeedback?.blockReasonMessage shouldBe "Reasons"
    }

    @Test
    fun `unknown model`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentStreamWithContent(
                content: List<*>,
                onResponse: (GenerateContentResponseObjc?) -> Unit,
                onComplete: (NSError?) -> Unit
            ) {
                readStreamingResponse(backend, "streaming-failure-unknown-model", onResponse, onComplete)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val responses = model.generateContentStream("prompt")
        shouldThrow<ServerException> { responses.collect() }
    }

    @Test
    fun `invalid api key`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun generateContentStreamWithContent(
                content: List<*>,
                onResponse: (GenerateContentResponseObjc?) -> Unit,
                onComplete: (NSError?) -> Unit
            ) {
                readStreamingResponse(backend, "streaming-failure-api-key", onResponse, onComplete)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val responses = model.generateContentStream("prompt")
        shouldThrow<InvalidAPIKeyException> { responses.collect() }
    }



}