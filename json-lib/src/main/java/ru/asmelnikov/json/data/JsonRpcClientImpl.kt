package ru.asmelnikov.json.data

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.asmelnikov.json.domain.JsonRpcClient
import ru.asmelnikov.json.domain.NetworkRequestException
import ru.asmelnikov.json.domain.TransportException
import ru.asmelnikov.json.domain.parser.RequestConverter
import ru.asmelnikov.json.domain.parser.ResponseParser
import ru.asmelnikov.json.domain.protocol.JsonRpcRequest
import ru.asmelnikov.json.domain.protocol.JsonRpcResponse

class JsonRpcClientImpl(
    private val baseurl: String,
    private val okHttpClient: OkHttpClient,
    private val requestConverter: RequestConverter,
    private val responseParser: ResponseParser
) : JsonRpcClient {
    override fun call(jsonRpcRequest: JsonRpcRequest): JsonRpcResponse {
        val requestBody = requestConverter.convert(jsonRpcRequest).toByteArray().toRequestBody()
        val request = Request.Builder()
            .post(requestBody)
            .url(baseurl)
            .build()
        val response = try {
            okHttpClient.newCall(request).execute()
        } catch (e: Exception) {
            throw NetworkRequestException(
                message = "Network error: ${e.message}",
                cause = e
            )
        }
        return if (response.isSuccessful) {
            response.body?.let { responseParser.parse(it.bytes()) }
                ?: throw java.lang.IllegalStateException("Response body is null")
        } else {
            throw TransportException(
                httpCode = response.code,
                message = "HTTP ${response.code}. ${response.message}",
                response = response
            )
        }
    }
}