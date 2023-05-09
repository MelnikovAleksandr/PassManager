package ru.asmelnikov.passmanager.ui

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.passmanager.data.Resource
import ru.asmelnikov.passmanager.data.model.ApiResponse
import ru.asmelnikov.passmanager.data.model.RandomSideEffects
import ru.asmelnikov.passmanager.data.model.RandomState
import ru.asmelnikov.passmanager.data.repository.RandomRepository

class MainViewModel(
    private val repository: RandomRepository
) : ViewModel(), ContainerHost<RandomState, RandomSideEffects> {

    override val container = container<RandomState, RandomSideEffects>(RandomState())

    fun loadRandom(apiResponse: ApiResponse) = intent {
        reduce { state.copy(isLoading = true, result = Resource.Loading()) }
        val result = repository.getRandom(apiResponse)
        reduce {
            state.copy(
                result = result,
                isLoading = false
            )
        }
    }
}