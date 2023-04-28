package ru.asmelnikov.json.domain.parser

import ru.asmelnikov.json.domain.protocol.JsonRpcResponse

interface ResponseParser {
    fun parse(data: ByteArray): JsonRpcResponse
}