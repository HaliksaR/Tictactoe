package ru.haliksar.tictactoe.core

import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.reflect.KClass


class Service(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient,
) {

    private companion object {

        val HOSTS = setOf(
            "http://192.168.31.240:8080",
            "http://192.168.31.240:8090",
            "http://192.168.31.240:8091"
        )
    }

    private fun <T : Any> provideApi(
        api: KClass<T>,
        url: String
    ): T = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(nullOnEmptyConverterFactory)
        .client(okHttpClient)
        .baseUrl(url)
        .build()
        .create(api.java)

    val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter =
                retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

            override fun convert(value: ResponseBody) =
                if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
        }
    }

    suspend operator fun <T : Any, R> invoke(
        api: KClass<T>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        invoker: suspend T.() -> R
    ): R = withContext(dispatcher) {
        HOSTS.forEach { host ->
            try {
                val service = provideApi(api, host)
                return@withContext invoker(service)
            } catch (throwable: Throwable) {
                if (throwable !is SocketTimeoutException) {
                    throw throwable
                }
            }
        }
        throw UnknownHostException("All host disabled!")
    }
}