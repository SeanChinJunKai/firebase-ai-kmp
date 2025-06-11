package io.github.seanchinjunkai.firebase.ai.type

sealed class FirebaseAIException(message: String, cause: Throwable? = null): Exception(message, cause)

public class SerializationException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class ServerException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class InvalidAPIKeyException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class PromptBlockedException(public val response: GenerateContentResponse?, message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class UnsupportedUserLocationException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class InvalidStateException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class ResponseStoppedException(public val response: GenerateContentResponse?, message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class RequestTimeoutException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class ServiceDisabledExceptionException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class ContentBlockedException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class QuotaExceededException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class InvalidLocationException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)

public class UnknownException(message: String, cause: Throwable? = null): FirebaseAIException(message, cause)
