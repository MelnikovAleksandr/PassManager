package ru.asmelnikov.json.data

import ru.asmelnikov.json.domain.JsonRpcClient
import ru.asmelnikov.json.domain.JsonRpcInterceptor
import ru.asmelnikov.json.domain.protocol.JsonRpcResponse

class ServerCallInterceptor(
    private val client: JsonRpcClient
) : JsonRpcInterceptor {
    override fun intercept(chain: JsonRpcInterceptor.Chain): JsonRpcResponse {
        return client.call(chain.request())
    }
}