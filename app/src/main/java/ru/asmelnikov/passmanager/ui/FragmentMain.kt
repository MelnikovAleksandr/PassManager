package ru.asmelnikov.passmanager.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.asmelnikov.json.data.JsonRpcClientImpl
import ru.asmelnikov.json.data.createJsonRpcService
import ru.asmelnikov.json.domain.JsonRpc
import ru.asmelnikov.json.domain.JsonRpcException
import ru.asmelnikov.json.domain.JsonRpcInterceptor
import ru.asmelnikov.json.domain.protocol.JsonRpcResponse
import ru.asmelnikov.moshi_json_parser.MoshiRequestConverter
import ru.asmelnikov.moshi_json_parser.MoshiResponseParser
import ru.asmelnikov.moshi_json_parser.MoshiResultParser
import ru.asmelnikov.passmanager.data.RandomApi
import ru.asmelnikov.passmanager.databinding.FragmentMainBinding

class FragmentMain : Fragment() {

    companion object {
        const val BASE_URL = "https://api.random.org/json-rpc/4/invoke"
        const val API_KEY = "3083b38c-1330-4284-9954-1e68aaa1ac51"
        const val CHARACTERS = "abcdefghijklmnopqrstuvwxyz123456789"
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var randomApi: RandomApi
    private lateinit var randomApiWithInterceptor: RandomApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initJsonRpcLibrary()

        binding.button.setOnClickListener {
            loadRandom(
                n = 1,
                length = 10,
                replacement = true,
                characters = CHARACTERS,
                apiKey = API_KEY
            )
        }

    }

    private fun loadRandom(
        apiKey: String,
        n: Int,
        length: Int,
        characters: String,
        replacement: Boolean,
        api: RandomApi = randomApiWithInterceptor
    ) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val random = api.getRandom(
                        n = n,
                        length = length,
                        replacement = replacement,
                        characters = characters,
                        apiKey = apiKey
                    )

                    withContext(Dispatchers.Main) {
                        binding.resultTextView.text = random.random.data.toString().replace("[\\[\\]]".toRegex(), "")
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    if (e is JsonRpcException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "JSON-RPC error with code ${e.code} and message ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()

                            binding.resultTextView.text = e.toString()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                e.message ?: e.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun initJsonRpcLibrary() {
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

        randomApiWithInterceptor = createJsonRpcService(
            service = RandomApi::class.java,
            client = jsonRpcClient,
            resultParser = MoshiResultParser(),
            interceptors = listOf(interceptor)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}