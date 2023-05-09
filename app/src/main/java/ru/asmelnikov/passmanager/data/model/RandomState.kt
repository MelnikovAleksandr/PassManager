package ru.asmelnikov.passmanager.data.model

import ru.asmelnikov.passmanager.data.Resource

data class RandomState(
    var result: Resource<ApiResult>? = null,
    val isLoading: Boolean = false
)

sealed class RandomSideEffects {
    data class Toast(val text: String) : RandomSideEffects()
}

