package br.com.eventlist.presenter.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class ValidatorTest {

    @Test
    fun `validate correct email, returns true`() {
        assertThat(Validator.isEmailValid("teste@email.com")).isTrue()
    }

    @Test
    fun `validate email empty, returns false`() {
        assertThat(Validator.isEmailValid("")).isFalse()
    }

    @Test
    fun `validate wrong email, returns false`() {
        assertThat(Validator.isEmailValid("@email.com")).isFalse()
    }

    @Test
    fun `validate password greater than 5, returns false`() {
        assertThat(Validator.isPasswordValid("1234")).isFalse()
    }

    @Test
    fun `validate password greater than 5, returns true`() {
        assertThat(Validator.isPasswordValid("1234567")).isTrue()
    }
}