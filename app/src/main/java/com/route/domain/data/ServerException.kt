package com.route.domain.data

/**
 * The server rejected a request. [serverMessage] is the server's own explanation when it sent one
 * (for example "Incorrect email or password"), so the UI can show it; null means it gave none.
 */
class ServerException(
    val serverMessage: String?,
    cause: Throwable? = null,
) : Exception(serverMessage, cause)
