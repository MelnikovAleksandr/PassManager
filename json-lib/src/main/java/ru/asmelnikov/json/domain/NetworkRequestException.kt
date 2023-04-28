package ru.asmelnikov.json.domain

class NetworkRequestException(
    override val message: String?,
    override val cause: Throwable
) : RuntimeException(message, cause)