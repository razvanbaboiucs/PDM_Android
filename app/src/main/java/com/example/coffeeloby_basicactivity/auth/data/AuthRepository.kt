package com.example.coffeeloby_basicactivity.auth.data

import android.content.SharedPreferences
import android.content.Context
import com.example.coffeeloby_basicactivity.auth.data.remote.RemoteAuthDataSource
import com.example.coffeeloby_basicactivity.core.Api
import com.example.coffeeloby_basicactivity.core.Result

object AuthRepository {
    var prefs: SharedPreferences? = null

    var user: User? = null
        private set

    fun isLoggedIn(context: Context): Boolean {
        if (prefs == null)
            prefs = context.getSharedPreferences("com.example.coffeeloby_basicactivity", Context.MODE_PRIVATE)
        val token = prefs!!.getString("token", "")!!
        if (token != "")
            Api.tokenInterceptor.token = token

        return token != ""
    }


    init {
        user = null
    }

    fun logout() {
        user = null
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String, context: Context): Result<TokenHolder> {
        val user = User(username, password)
        val result = RemoteAuthDataSource.login(user)
        if (result is Result.Success<TokenHolder>) {
            setLoggedInUser(user, result.data, context)
        }

        if (prefs == null)
            prefs = context.getSharedPreferences("com.example.coffeeloby_basicactivity", Context.MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putString("username", username)
        editor.apply()
        return result
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder, context: Context) {
        this.user = user
        Api.tokenInterceptor.token = tokenHolder.token

        if (prefs == null)
            prefs = context.getSharedPreferences("com.example.coffeeloby_basicactivity", Context.MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putString("token", tokenHolder.token)
        editor.apply()
    }
}
