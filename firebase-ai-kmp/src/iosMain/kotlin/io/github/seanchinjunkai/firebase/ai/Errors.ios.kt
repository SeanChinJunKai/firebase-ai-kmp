@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.FirebaseAIException
import platform.Foundation.NSError
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcSerialization
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcServer
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcInvalidAPIKey
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcPromptBlocked
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcUnsupportedUserLocation
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcContentBlocked
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcQuotaExceeded
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcResponseStopped
import cocoapods.FirebaseAIBridge.FirebaseAIErrorObjcServiceDisabled
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc as iOSGenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.ContentBlockedException
import io.github.seanchinjunkai.firebase.ai.type.InvalidAPIKeyException
import io.github.seanchinjunkai.firebase.ai.type.PromptBlockedException
import io.github.seanchinjunkai.firebase.ai.type.QuotaExceededException
import io.github.seanchinjunkai.firebase.ai.type.ResponseStoppedException
import io.github.seanchinjunkai.firebase.ai.type.SerializationException
import io.github.seanchinjunkai.firebase.ai.type.ServerException
import io.github.seanchinjunkai.firebase.ai.type.ServiceDisabledException
import io.github.seanchinjunkai.firebase.ai.type.UnknownException
import io.github.seanchinjunkai.firebase.ai.type.UnsupportedUserLocationException
import kotlinx.cinterop.ExperimentalForeignApi


public fun NSError.toFirebaseAIException(): FirebaseAIException {
    val message = this.localizedDescription
    return when (this.code) {
        FirebaseAIErrorObjcSerialization -> SerializationException(message)
        FirebaseAIErrorObjcServer -> ServerException(message)
        FirebaseAIErrorObjcInvalidAPIKey -> InvalidAPIKeyException(message)
        FirebaseAIErrorObjcPromptBlocked -> {
            val response = this.userInfo["response"] as? iOSGenerateContentResponse
            PromptBlockedException(response?.toGenerateContentResponse(), message)
        }
        FirebaseAIErrorObjcUnsupportedUserLocation -> UnsupportedUserLocationException(message)
        FirebaseAIErrorObjcResponseStopped -> {
            val response = this.userInfo["response"] as? iOSGenerateContentResponse
            ResponseStoppedException(response?.toGenerateContentResponse(), message)
        }
        FirebaseAIErrorObjcServiceDisabled -> ServiceDisabledException(message)
        FirebaseAIErrorObjcContentBlocked -> ContentBlockedException(message)
        FirebaseAIErrorObjcQuotaExceeded -> QuotaExceededException(message)
        else -> UnknownException(message)
    }
}