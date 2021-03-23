package ru.haliksar.tictactoe.core

import android.view.View
import android.widget.EditText
import androidx.annotation.MainThread
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


@MainThread
@ExperimentalCoroutinesApi
fun EditText.asFlow(): Flow<String?> = callbackFlow {
    val watcher = doAfterTextChanged { offer(it?.toString()) }
    awaitClose { removeTextChangedListener(watcher) }
}

@MainThread
@ExperimentalCoroutinesApi
fun View.bindClick(): Flow<Unit> = callbackFlow {
    val listener = View.OnClickListener { offer(Unit) }
    setOnClickListener(listener)
    awaitClose { setOnClickListener(listener) }
}