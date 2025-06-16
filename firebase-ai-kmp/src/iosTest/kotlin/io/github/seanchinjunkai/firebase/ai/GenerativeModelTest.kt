@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebaseAIBridge.CountTokensResponseObjc as iOSCountTokensResponse
import cocoapods.FirebaseAIBridge.GenerativeModelObjc as iOSGenerativeModel
import kotlinx.coroutines.test.runTest
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import platform.Foundation.NSError

class iOSGenerativeModelTest {
    @Test
    fun `countTokens success and returns both totalTokens and totalBillableCharacters`() = runTest {
        val fakeFirebaseiOSModel = object : iOSGenerativeModel() {
            override fun countTokensWithPrompt(
                prompt: String,
                completionHandler: (iOSCountTokensResponse?, NSError?) -> Unit
            ) {
                val response = readCountTokensResponse("unary-success-total-tokens.json")
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
                val response = readCountTokensResponse("unary-success-no-billable-characters.json")
                completionHandler(response, null)
            }
        }
        val model = GenerativeModel(fakeFirebaseiOSModel)
        val response = model.countTokens("test prompt")
        assert(response.totalTokens == 258)
        assert(response.totalBillableCharacters == null)
    }
}


