package br.com.eventlist.presenter.login

/**
 * State de validação de dados do login
 */
data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
/**
 * State de validação de dados do formulário de cadastro
 */
data class RegisterFormState(
    val usernameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val setPasswordError: Int? = null,
    val isDataValid: Boolean = false
)