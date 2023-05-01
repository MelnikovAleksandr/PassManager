package ru.asmelnikov.passmanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.asmelnikov.json.domain.JsonRpcException
import ru.asmelnikov.passmanager.data.Resource
import ru.asmelnikov.passmanager.data.model.ApiResponse
import ru.asmelnikov.passmanager.data.model.ApiResult
import ru.asmelnikov.passmanager.data.repository.RandomRepository

class MainViewModel(
    private val repository: RandomRepository
) : ViewModel() {

    private var _randomLiveData = MutableLiveData<Resource<ApiResult>>()

    val randomLiveData: LiveData<Resource<ApiResult>>
        get() = _randomLiveData

    fun loadRandom(
        apiResponse: ApiResponse
    ) {
        viewModelScope.launch {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    _randomLiveData.postValue(Resource.Loading())
                    val result = repository.getRandom(apiResponse)
                    CoroutineScope(Dispatchers.Main).launch {
                        _randomLiveData.value = result
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                if (e is JsonRpcException) {
                    CoroutineScope(Dispatchers.Main).launch {
//                            Toast.makeText(
//                                requireContext(),
//                                "JSON-RPC error with code ${e.code} and message ${e.message}",
//                                Toast.LENGTH_LONG
//                            ).show()

                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
//                        Toast.makeText(
//                            requireContext(),
//                            e.message ?: e.toString(),
//                            Toast.LENGTH_LONG
//                        ).show()
                    }
                }
            }
        }
    }
}