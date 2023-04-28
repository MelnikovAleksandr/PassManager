package ru.asmelnikov.json.domain.parser

import ru.asmelnikov.json.domain.protocol.JsonRpcRequest

interface RequestConverter {
    fun convert(request: JsonRpcRequest): String
}