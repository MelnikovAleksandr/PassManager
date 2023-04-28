package ru.asmelnikov.json.domain.protocol

data class JsonRpcResponse(
    val id: Long,
    val result: Any?,
    val error: JsonRpcError?
)
