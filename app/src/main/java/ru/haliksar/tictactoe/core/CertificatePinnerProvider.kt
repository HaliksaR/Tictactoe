package ru.haliksar.tictactoe.core

import okhttp3.CertificatePinner


internal const val SSL_SHA256_PIN = "sha256/WoiDFDFa9iGFHDFTFGFDliYS9VwdfDd4PB18=" // example

internal fun provideCertificatePinner(
    host: String,
    mock: Boolean
): CertificatePinner =
    if (!mock) {
        CertificatePinner.Builder()
//			.add(host, SSL_SHA256_PIN) // example
            .build()
    } else {
        CertificatePinner.DEFAULT
    }