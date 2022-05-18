package br.com.eventlist.data.repository

import android.content.Context
import android.content.SharedPreferences
import br.com.eventlist.R
import br.com.eventlist.domain.model.User
import br.com.eventlist.presenter.model.LoggedInUser

class LoginRepository(
    private val context: Context,
    private val sharedPref: SharedPreferences,
) {

    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
    }

    fun login(
        email: String,
        password: String
    ): LoggedInUser {

        val result = getLoggedInUser()

        return if (result == null) {
            this.user = null
            LoggedInUser(result, false)
        } else {
            this.user = result
            LoggedInUser(result, (result.password != password || result.email != email))
        }
    }

    fun register(
        username: String,
        email: String,
        password: String
    ): User? {

        val result = saveUser(username, email, password)
        return if (result) {
            this.user = getLoggedInUser()
            this.user
        } else {
            null
        }
    }

    private fun saveUser(
        username: String,
        email: String,
        password: String
    ): Boolean {
        return try {
            with(sharedPref.edit()) {
                putString(context.getString(R.string.saved_username_key), username)
                putString(context.getString(R.string.saved_email_key), email)
                putString(context.getString(R.string.saved_password_key), password)
                apply()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadUser() {
        this.user = getLoggedInUser()
    }

    private fun getLoggedInUser(): User? {
        return try {
            val username =
                sharedPref.getString(context.getString(R.string.saved_username_key), null)
            val email = sharedPref.getString(context.getString(R.string.saved_email_key), null)
            val password =
                sharedPref.getString(context.getString(R.string.saved_password_key), null)
            if (username == null && email == null && password == null) null
            else User(username!!, email!!, password!!)
        } catch (e: Exception) {
            null
        }
    }
}