package ru.asmelnikov.passmanager.data.model

data class ApiResponse(
    val apiKey: String,
    val n: Int,
    val length: Int,
    val characters: String,
    val replacement: Boolean
)