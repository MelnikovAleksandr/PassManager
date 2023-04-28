package ru.asmelnikov.json.domain.protocol

data class JsonRpcRequest(
    val id: Long,
    val method: String,
    val params: Map<String, Any?> = emptyMap(),
    val jsonRpc: String = "2.0"
)