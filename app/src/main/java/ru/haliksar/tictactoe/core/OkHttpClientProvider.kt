package ru.haliksar.tictactoe.core

import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

private const val VALUE_TIMEOUT: Long = 5

fun provideOkHttpClient(
	interceptors: List<Interceptor> = emptyList(),
	authenticators: List<Authenticator> = emptyList(),
): OkHttpClient = okHttpClientSetups(interceptors, authenticators)

internal fun loggingInterceptor(): Interceptor =
	HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

internal fun okHttpClientSetups(
	interceptors: List<Interceptor> = emptyList(),
	authenticators: List<Authenticator> = emptyList(),
): OkHttpClient =
	OkHttpClient().newBuilder()
		.apply {
			connectTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
			writeTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
			readTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
			addInterceptor(loggingInterceptor())
			interceptors.forEach { addInterceptor(it) }
			authenticators.forEach { authenticator(it) }
		}
		.build()