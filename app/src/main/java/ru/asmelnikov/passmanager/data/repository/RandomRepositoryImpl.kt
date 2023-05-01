package ru.asmelnikov.passmanager.data.repository

import ru.asmelnikov.passmanager.data.RandomApi
import ru.asmelnikov.passmanager.data.Resource
import ru.asmelnikov.passmanager.data.model.ApiResponse
import ru.asmelnikov.passmanager.data.model.ApiResult

class RandomRepositoryImpl(
    private val api: RandomApi
) : RandomRepository {
    override suspend fun getRandom(apiResponse: ApiResponse): Resource<ApiResult> {
        return try {
            Resource.Success(
                api.getRandom(
                    apiResponse.apiKey,
                    apiResponse.n,
                    apiResponse.length,
                    apiResponse.characters,
                    apiResponse.replacement
                )
            )
        } catch (error: Throwable) {
            Resource.Error(error.localizedMessage ?: "An unexpected error")
        }
    }
}