package ru.asmelnikov.passmanager.data.repository

import ru.asmelnikov.passmanager.data.Resource
import ru.asmelnikov.passmanager.data.model.ApiResponse
import ru.asmelnikov.passmanager.data.model.ApiResult

interface RandomRepository {
    suspend fun getRandom(apiResponse: ApiResponse): Resource<ApiResult>
}