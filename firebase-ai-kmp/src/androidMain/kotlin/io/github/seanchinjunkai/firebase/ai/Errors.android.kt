package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.ContentBlockedException
import com.google.firebase.ai.type.FirebaseAIException as AndroidFirebaseAIException
import com.google.firebase.ai.type.SerializationException as AndroidSerializationException
import com.google.firebase.ai.type.ServerException as AndroidServerException
import com.google.firebase.ai.type.InvalidAPIKeyException as AndroidInvalidAPIKeyException
import com.google.firebase.ai.type.PromptBlockedException as AndroidPromptBlockedException
import com.google.firebase.ai.type.UnsupportedUserLocationException as AndroidUnsupportedUserLocationException
import com.google.firebase.ai.type.InvalidStateException as AndroidInvalidStateException
import com.google.firebase.ai.type.ResponseStoppedException as AndroidResponseStoppedException
import com.google.firebase.ai.type.RequestTimeoutException as AndroidRequestTimeoutException
import com.google.firebase.ai.type.ServiceDisabledException as AndroidServiceDisabledException
import com.google.firebase.ai.type.ContentBlockedException as AndroidContentBlockedException
import com.google.firebase.ai.type.QuotaExceededException as AndroidQuotaExceededException
import com.google.firebase.ai.type.InvalidLocationException as AndroidInvalidLocationException

import io.github.seanchinjunkai.firebase.ai.type.FirebaseAIException
import io.github.seanchinjunkai.firebase.ai.type.InvalidAPIKeyException
import io.github.seanchinjunkai.firebase.ai.type.InvalidLocationException
import io.github.seanchinjunkai.firebase.ai.type.InvalidStateException
import io.github.seanchinjunkai.firebase.ai.type.PromptBlockedException
import io.github.seanchinjunkai.firebase.ai.type.QuotaExceededException
import io.github.seanchinjunkai.firebase.ai.type.RequestTimeoutException
import io.github.seanchinjunkai.firebase.ai.type.ResponseStoppedException
import io.github.seanchinjunkai.firebase.ai.type.SerializationException
import io.github.seanchinjunkai.firebase.ai.type.ServerException
import io.github.seanchinjunkai.firebase.ai.type.ServiceDisabledException
import io.github.seanchinjunkai.firebase.ai.type.UnknownException
import io.github.seanchinjunkai.firebase.ai.type.UnsupportedUserLocationException

public fun AndroidFirebaseAIException.toFirebaseAIException(): FirebaseAIException {
    return when (this) {
        is AndroidSerializationException -> SerializationException(message ?: "", cause)
        is AndroidServerException -> ServerException(message ?: "", cause)
        is AndroidInvalidAPIKeyException -> InvalidAPIKeyException(message ?: "", cause)
        is AndroidPromptBlockedException -> PromptBlockedException(response?.toGenerateContentResponse(), message ?: "", cause)
        is AndroidUnsupportedUserLocationException -> UnsupportedUserLocationException(message ?: "", cause)
        is AndroidInvalidStateException -> InvalidStateException(message ?: "", cause)
        is AndroidResponseStoppedException -> ResponseStoppedException(response.toGenerateContentResponse(), message ?: "", cause)
        is AndroidRequestTimeoutException -> RequestTimeoutException(message ?: "", cause)
        is AndroidServiceDisabledException -> ServiceDisabledException(message ?: "", cause)
        is AndroidContentBlockedException -> ContentBlockedException(message ?: "", cause)
        is AndroidQuotaExceededException -> QuotaExceededException(message ?: "", cause)
        is AndroidInvalidLocationException -> InvalidLocationException(message ?: "", cause)
        else -> UnknownException(message ?: "", cause)
    }
}

