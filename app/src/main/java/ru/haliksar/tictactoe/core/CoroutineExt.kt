package ru.haliksar.tictactoe.core

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


fun CoroutineScope.launchCatching(
    job: suspend CoroutineScope.() -> Unit,
    catching: suspend (Throwable) -> Unit,
) = launch {
    try {
        job()
    } catch (throwable: Throwable) {
        catching(throwable)
    }
}

fun <T> Flow<T>.launchWhenStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted { collect() }
}