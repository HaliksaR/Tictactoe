package ru.haliksar.tictactoe.domain.usecese

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class GetUserIdUseCase(context: Context) {


    private companion object {

        const val USER_PREF = "USER_PREF"
        const val USER_ID = "USER_ID"
    }

    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
    }

    operator fun invoke(): String =
        pref.getString(USER_ID, null) ?: getUuid()

    private fun getUuid(): String {
        val uuid = UUID.randomUUID().toString()
        pref.edit().putString(USER_ID, uuid).apply()
        return uuid
    }
}