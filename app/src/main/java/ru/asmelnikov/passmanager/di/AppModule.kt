package ru.asmelnikov.passmanager.di

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.json.data.JsonRpcClientImpl
import ru.asmelnikov.json.data.createJsonRpcService
import ru.asmelnikov.json.domain.JsonRpcInterceptor
import ru.asmelnikov.json.domain.protocol.JsonRpcResponse
import ru.asmelnikov.moshi_json_parser.MoshiRequestConverter
import ru.asmelnikov.moshi_json_parser.MoshiResponseParser
import ru.asmelnikov.moshi_json_parser.MoshiResultParser
import ru.asmelnikov.passmanager.data.RandomApi
import ru.asmelnikov.passmanager.data.repository.RandomRepository
import ru.asmelnikov.passmanager.data.repository.RandomRepositoryImpl
import ru.asmelnikov.passmanager.ui.MainViewModel
import ru.asmelnikov.passmanager.utils.Constants.BASE_URL

val appModule = module {

    single {
        val logger = HttpLoggingInterceptor.Logger { Log.d("JSON-RPC", it) }
        val loginInterceptor =
            HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loginInterceptor)
            .build()

        val jsonRpcClient = JsonRpcClientImpl(
            baseurl = BASE_URL,
            okHttpClient = okHttpClient,
            requestConverter = MoshiRequestConverter(),
            responseParser = MoshiResponseParser()
        )

        val interceptor = object : JsonRpcInterceptor {
            override fun intercept(chain: JsonRpcInterceptor.Chain): JsonRpcResponse {
                val initialResponse = chain.proceed(chain.request())
                return if (initialResponse.error != null) {
                    chain.proceed(chain.request().copy(params = mapOf("id" to 1)))
                } else {
                    initialResponse
                }
            }
        }
        createJsonRpcService(
            service = RandomApi::class.java,
            client = jsonRpcClient,
            resultParser = MoshiResultParser(),
            interceptors = listOf(interceptor)
        )
    }

    single<RandomRepository> {
        RandomRepositoryImpl(get())
    }

    viewModel {
        MainViewModel(get())
    }
}