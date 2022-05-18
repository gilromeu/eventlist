package br.com.eventlist.presenter.login

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import br.com.eventlist.R
import br.com.eventlist.databinding.FragmentLoginBinding
import br.com.eventlist.presenter.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var loginButton: Button? = null
    private lateinit var load: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.titleLogin.text = requireContext().getString(R.string.login)
        load = binding.load.layoutLoadAnimation
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.loadUser()

        emailEditText = binding.email
        passwordEditText = binding.password
        loginButton = binding.loginButton

        observer(view)

        validate()
    }

    private fun observer(view: View) {

        loginViewModel.isLoggedIn.observe(viewLifecycleOwner) { resources ->

            when (resources.status) {
                Status.LOADING -> {
                    load.show()
                }
                Status.SUCCESS -> {
                    load.hide()
                    if (resources.data == true) {
                        view.findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToEventsFragment()
                        )
                    }
                }
                Status.ERROR -> {
                    load.hide()
                    showLoginFailed(requireContext(), resources.message ?: "")
                }
            }
        }

        loginViewModel.loginFormState.observe(viewLifecycleOwner) { loginFormState ->
            if (loginFormState == null) {
                return@observe
            }
            loginButton?.isEnabled = loginFormState.isDataValid

            loginFormState.emailError?.let {
                emailEditText?.error = requireContext().getString(it)
            }
            loginFormState.passwordError?.let {
                passwordEditText?.error = requireContext().getString(it)
            }
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) { resources ->

            when (resources.status) {
                Status.LOADING -> {
                    load.show()
                }
                Status.SUCCESS -> {
                    load.hide()
                    view.findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToEventsFragment()
                    )
                }
                Status.ERROR -> {
                    load.hide()
                    when (resources.data) {
                        ResultLogin.IS_INCORRECT_PASSWORD -> {
                            showLoginFailed(requireContext(), resources.message ?: "")
                        }
                        ResultLogin.REGISTER -> {
                            view.findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToRegisterFragment(
                                    email = emailEditText?.text.toString(),
                                    senha = passwordEditText?.text.toString()
                                )
                            )
                        }
                        else -> {
                            showLoginFailed(requireContext(), resources.message ?: "")
                        }
                    }
                }
            }
        }
    }

    private fun validate() {
        emailEditText?.afterTextChanged {
            loginViewModel.loginDataChanged(
                emailEditText?.text.toString(),
                passwordEditText?.text.toString()
            )
        }

        passwordEditText?.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    emailEditText?.text.toString(),
                    passwordEditText?.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            requireContext(),
                            emailEditText?.text.toString(),
                            passwordEditText?.text.toString()
                        )
                }
                false
            }

            loginButton?.setOnClickListener {
                load.show()
                loginViewModel.login(
                    requireContext(),
                    emailEditText?.text.toString(),
                    passwordEditText?.text.toString()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}