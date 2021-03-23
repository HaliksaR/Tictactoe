package ru.haliksar.tictactoe.core

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

internal fun provideMoshi(): Moshi = Moshi.Builder()
	.add(KotlinJsonAdapterFactory())
	.build()