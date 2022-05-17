package br.com.eventlist.presenter.model

import br.com.eventlist.domain.model.User

data class LoggedInUser(
    val user: User?,
    val isIncorrectPassword: Boolean,
)