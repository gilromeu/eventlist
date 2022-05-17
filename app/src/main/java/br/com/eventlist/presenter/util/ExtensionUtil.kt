package br.com.eventlist.presenter.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Função de extensão para simplificar a configuração de views
 */

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun ConstraintLayout.show() {
    this.visibility = View.VISIBLE
}

fun TextView.show() {
    this.visibility = View.VISIBLE
}

fun TextView.hide() {
    this.visibility = View.GONE
}

fun ConstraintLayout.hide() {
    Handler(Looper.myLooper()!!).postDelayed({
        this.animate()
            .translationY(0f)
            .alpha(0.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    visibility = View.GONE
                }
            })
    }, 3000)

}

fun showLoginFailed(context: Context, errorString: String) {
    Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
}