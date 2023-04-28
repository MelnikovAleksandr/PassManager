package ru.asmelnikov.json.domain

import ru.asmelnikov.json.domain.protocol.JsonRpcRequest
import ru.asmelnikov.json.domain.protocol.JsonRpcResponse

interface JsonRpcInterceptor {

    fun intercept(chain: Chain): JsonRpcResponse

    interface Chain {
        fun proceed(request: JsonRpcRequest): JsonRpcResponse

        fun request(): JsonRpcRequest
    }
}