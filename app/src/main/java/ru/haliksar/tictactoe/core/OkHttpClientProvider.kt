package ru.haliksar.tictactoe.core

import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private const val VALUE_TIMEOUT: Long = 60

fun provideOkHttpClient(
	interceptors: List<Interceptor> = emptyList(),
	authenticators: List<Authenticator> = emptyList(),
	certificatePinner: CertificatePinner
): OkHttpClient = okHttpClientSetups(interceptors, authenticators, certificatePinner)


internal fun okHttpClientSetups(
	interceptors: List<Interceptor> = emptyList(),
	authenticators: List<Authenticator> = emptyList(),
	certificatePinner: CertificatePinner,
): OkHttpClient =
	OkHttpClient().newBuilder()
		.apply {
			connectTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
			writeTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
			readTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
			interceptors.forEach { addInterceptor(it) }
			authenticators.forEach { authenticator(it) }
			certificatePinner(certificatePinner)
		}
		.build()