package ru.asmelnikov.passmanager.data

import com.squareup.moshi.Json

data class RandomResult(
    @Json(name = "data")
    val data: List<String>
)
