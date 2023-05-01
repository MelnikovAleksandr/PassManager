package ru.asmelnikov.passmanager.data.model

data class ApiResult(
    val random: RandomResult,
    val bitsUsed: Int,
    val bitsLeft: Int,
    val requestsLeft: Int,
    val advisoryDelay: Int
)

data class RandomResult(
    val data: List<String>,
    val completionTime: String
)