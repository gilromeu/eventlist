package br.com.eventlist.presenter.login

import android.content.Context
import androidx.lifecycle.*
import br.com.eventlist.R
import br.com.eventlist.data.repository.LoginRepository
import br.com.eventlist.presenter.util.Resource
import br.com.eventlist.presenter.util.ResultLogin
import br.com.eventlist.presenter.util.Validator.isEmailValid
import br.com.eventlist.presenter.util.Validator.isPasswordValid

class LoginViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Resource<ResultLogin>>()
    val loginResult: LiveData<Resource<ResultLogin>> = _loginResult

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<Resource<ResultLogin>>()
    val registerResult: LiveData<Resource<ResultLogin>> = _registerResult

    private val _isLoggedIn = MutableLiveData<Resource<Boolean>>()
    val isLoggedIn: LiveData<Resource<Boolean>> = _isLoggedIn

    fun loadUser() {
        loginRepository.loadUser()
        _isLoggedIn.value = Resource.success(loginRepository.isLoggedIn)
    }

    fun login(context: Context, email: String, password: String) {

        val result = loginRepository.login(email, password)

        if (result.user != null) {
            if (result.isIncorrectPassword) {
                _loginResult.value = Resource.error(
                    context.getString(R.string.invalid_login),
                    ResultLogin.IS_INCORRECT_PASSWORD
                )
            } else {
                _loginResult.value = Resource.success(ResultLogin.SUCCESS)
            }
        } else {
            _loginResult.value = Resource.error(
                context.getString(R.string.no_record),
                ResultLogin.REGISTER
            )
        }
    }

    fun register(
        context: Context,
        username: String,
        email: String,
        password: String
    ) {
        _registerResult.value = Resource.loading(null)
        val result = loginRepository.register(username, email, password)
        if (result == null) {
            _registerResult.value = Resource.error(
                context.getString(R.string.failure_register), ResultLogin.ERROR
            )
        } else {
            _registerResult.value = Resource.success(ResultLogin.SUCCESS)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isEmailValid(username)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun registryDataChanged(
        username: String,
        email: String,
        password: String,
        setPassword: String
    ) {
        if (username.isEmpty() && username.isBlank()) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_empty)
        } else if (!isEmailValid(email)) {
            _registerForm.value = RegisterFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password, setPassword)) {
            if (password.length > 5) {
                _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
            } else {
                _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password_equal)
            }
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }
}