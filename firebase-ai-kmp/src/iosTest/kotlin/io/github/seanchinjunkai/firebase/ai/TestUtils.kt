@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.CountTokensResponseObjc
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import kotlin.experimental.ExperimentalNativeApi
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfURL
import platform.Foundation.NSUTF8StringEncoding

val json = Json {
    ignoreUnknownKeys = true
}

fun readJsonFile(resourceName: String): String {
    val path = NSBundle.mainBundle.pathForResource("resources/$resourceName", "json") ?: ""
    val fileURL = NSURL.fileURLWithPath(path)
    val content = NSString.stringWithContentsOfURL(fileURL, NSUTF8StringEncoding, null) ?: ""
    return content
}

fun readErrorResponse(resourceName: String): NSError {
    val jsonString = readJsonFile(resourceName)
    return json.decodeFromString(NSErrorSerializer, jsonString)
}

fun readCountTokensResponse(resourceName: String): CountTokensResponseObjc {
    val jsonString = readJsonFile(resourceName)
    return json.decodeFromString(CountTokensResponseObjcSerializer, jsonString)
}

fun readGenerateContentResponse(resourceName: String): GenerateContentResponseObjc {
    val jsonString = readJsonFile(resourceName)
    return json.decodeFromString(GenerateContentResponseObjcSerializer, jsonString)
}