package ru.haliksar.tictactoe.data.datasource

import android.content.Context
import android.content.SharedPreferences
import ru.haliksar.tictactoe.domain.entity.Player
import java.util.*

interface UserLocalDataSource {

    fun setNickName(value: String)

    fun getUser(): Player

    fun getUuid(): String
}

class UserLocalDataSourceImpl(
    context: Context
) : UserLocalDataSource {

    private companion object {

        const val USER_PREF = "USER_PREF"
        const val USER_ID = "USER_ID"
        const val USER_NICKNAME = "USER_NICKNAME"
        val DEFAULT_NICKNAMES = setOf(
            "Dog",
            "Kapibara",
            "Owl",
            "Cat",
            "Perrot",
        )
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
    }

    override fun setNickName(value: String) {
        preferences.edit().putString(USER_NICKNAME, value).apply()
    }

    override fun getUser(): Player {
        val nickname = preferences.getString(USER_NICKNAME, null)
            ?: DEFAULT_NICKNAMES.random()
        return Player(
            userId = getUuid(),
            nickname = nickname,
        )
    }

    override fun getUuid(): String {
        val id = preferences.getString(USER_ID, null)
        return if (id == null) {
            val uuid = UUID.randomUUID().toString()
            preferences.edit().putString(USER_ID, uuid).apply()
            return uuid
        } else {
            id
        }
    }

}