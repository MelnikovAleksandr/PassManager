package ru.asmelnikov.passmanager.data

import ru.asmelnikov.json.domain.JsonRpc

interface RandomApi {

    @JsonRpc("generateStrings")
    fun getRandom(
        @JsonRpc("apiKey") apiKey: String,
        @JsonRpc("n") n: Int,
        @JsonRpc("length") length: Int,
        @JsonRpc("characters") characters: String,
        @JsonRpc("replacement") replacement: Boolean
    ): ApiResult
}