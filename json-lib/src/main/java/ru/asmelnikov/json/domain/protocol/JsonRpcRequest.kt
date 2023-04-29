package ru.asmelnikov.json.domain.protocol

data class JsonRpcRequest(
    val jsonrpc: String = "2.0",
    val id: Long,
    val method: String,
    val params: Map<String, Any?> = emptyMap(),
)