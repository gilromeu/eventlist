package br.com.eventlist.presenter.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import br.com.eventlist.R
import br.com.eventlist.databinding.FragmentRegisterBinding
import br.com.eventlist.presenter.util.*

class RegisterFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private val args: RegisterFragmentArgs by navArgs()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var usernameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var setPasswordEditText: EditText? = null
    private lateinit var load: ConstraintLayout
    private var registerButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.titleRegister.text = requireContext().getString(R.string.register)
        load = binding.load.layoutLoadAnimation
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this,
            LoginViewModel.LoginViewModelFactory()
        )[LoginViewModel::class.java]

        usernameEditText = binding.username
        emailEditText = binding.email
        passwordEditText = binding.password
        setPasswordEditText = binding.setPassword
        registerButton = binding.registerButton

        emailEditText?.setText(args.email)
        passwordEditText?.setText(args.senha)

        registerButton?.setOnClickListener {
            loginViewModel.register(
                requireContext(),
                requireActivity().getPreferences(Context.MODE_PRIVATE),
                usernameEditText?.text.toString(),
                emailEditText?.text.toString(),
                passwordEditText?.text.toString()
            )
        }

        observer(view)

        validate()

        load.hide()
    }

    private fun observer(view: View) {
        loginViewModel.registerFormState.observe(viewLifecycleOwner) { registerFormState ->
            if (registerFormState == null) {
                return@observe
            }
            registerButton?.isEnabled = registerFormState.isDataValid

            registerFormState.usernameError?.let {
                usernameEditText?.error = requireContext().getString(it)
            }
            registerFormState.emailError?.let {
                emailEditText?.error = requireContext().getString(it)
            }
            registerFormState.passwordError?.let {
                passwordEditText?.error = requireContext().getString(it)
            }
            registerFormState.setPasswordError?.let {
                setPasswordEditText?.error = requireContext().getString(it)
            }
        }

        loginViewModel.registerResult.observe(viewLifecycleOwner) { resources ->
            when(resources.status) {
                Status.LOADING -> {
                    load.show()
                }
                Status.SUCCESS -> {
                    load.hide()
                    view.findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToEventsFragment()
                    )
                }
                Status.ERROR -> {
                    load.hide()
                    showLoginFailed(requireContext(), resources.message ?: "")
                }
            }
        }
    }

    private fun validate() {
        usernameEditText?.afterTextChanged {
            loginViewModel.registryDataChanged(
                usernameEditText?.text.toString(),
                emailEditText?.text.toString(),
                passwordEditText?.text.toString(),
                setPasswordEditText?.text.toString()
            )
        }

        emailEditText?.afterTextChanged {
            loginViewModel.registryDataChanged(
                usernameEditText?.text.toString(),
                emailEditText?.text.toString(),
                passwordEditText?.text.toString(),
                setPasswordEditText?.text.toString()
            )
        }

        passwordEditText?.afterTextChanged {
            loginViewModel.registryDataChanged(
                usernameEditText?.text.toString(),
                emailEditText?.text.toString(),
                passwordEditText?.text.toString(),
                setPasswordEditText?.text.toString()
            )
        }

        setPasswordEditText?.afterTextChanged {
            loginViewModel.registryDataChanged(
                usernameEditText?.text.toString(),
                emailEditText?.text.toString(),
                passwordEditText?.text.toString(),
                setPasswordEditText?.text.toString()
            )
        }
    }
}