@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.CountTokensResponseObjc
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.experimental.ExperimentalNativeApi
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.NSUTF8StringEncoding

val json = Json {
    ignoreUnknownKeys = true
}


fun readStreamingResponse(resourceName: String, onResponse: (GenerateContentResponseObjc?) -> Unit, onComplete: (NSError?) -> Unit) {
    val path = NSBundle.mainBundle.pathForResource("resources/$resourceName", "txt") ?: ""
    val content = NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null) ?: ""
    val lines = content
        .split("\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    for (line in lines) {
        if (line.startsWith("data:")) {
            val jsonString = line.substringAfter("data:").trim()
            val response = json.decodeFromString(GenerateContentResponseObjcSerializer, jsonString)
            onResponse(response)
        } else {
            val error = json.decodeFromString(NSErrorSerializer, line)
            onComplete(error)
        }
    }
}

inline fun <reified T> readUnaryResponse(backend: String, resourceName: String, ext: String = "json", serializer: KSerializer<T>): T {
    val path = NSBundle.mainBundle.pathForResource("resources/$backend/$resourceName", ext) ?: error("Resource not found: $resourceName.$ext")
    val content = NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null) ?: error("Unable to read content of: $resourceName.$ext")
    return json.decodeFromString(serializer, content)
}

fun readErrorResponse(backend: String, resourceName: String): NSError =
    readUnaryResponse(backend, resourceName, serializer = NSErrorSerializer)

fun readCountTokensResponse(backend: String, resourceName: String): CountTokensResponseObjc =
    readUnaryResponse(backend,resourceName, serializer = CountTokensResponseObjcSerializer)

fun readGenerateContentResponse(backend: String, resourceName: String): GenerateContentResponseObjc =
    readUnaryResponse(backend, resourceName, serializer = GenerateContentResponseObjcSerializer)