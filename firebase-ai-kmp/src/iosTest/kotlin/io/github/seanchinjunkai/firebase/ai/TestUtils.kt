@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.CountTokensResponseObjc
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import kotlin.experimental.ExperimentalNativeApi
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.dataWithContentsOfFile

val json = Json {
    ignoreUnknownKeys = true
}

fun readJsonFile(resourceName: String): String {
    val path = NSBundle.mainBundle.pathForResource("resources/$resourceName", "json") ?: ""
    val data = NSData.dataWithContentsOfFile(path)
    val byteArray = data?.toByteArray()!!
    return byteArray.decodeToString()
}

fun readErrorResponse(resourceName: String): NSError {
    val jsonString = readJsonFile(resourceName)
    println(jsonString)
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

