package ru.asmelnikov.json.domain

import ru.asmelnikov.json.domain.protocol.JsonRpcRequest
import ru.asmelnikov.json.domain.protocol.JsonRpcResponse

interface JsonRpcClient {

    fun call(jsonRpcRequest: JsonRpcRequest): JsonRpcResponse

}