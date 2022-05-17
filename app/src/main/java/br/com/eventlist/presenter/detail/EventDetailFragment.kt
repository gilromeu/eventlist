package br.com.eventlist.presenter.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import br.com.eventlist.R
import br.com.eventlist.data.repository.EventRepositoryImpl
import br.com.eventlist.data.api.EventApi
import br.com.eventlist.data.api.ServiceApi
import br.com.eventlist.data.repository.LoginRepository
import br.com.eventlist.databinding.FragmentEventDetailBinding
import br.com.eventlist.domain.model.CheckIn
import br.com.eventlist.domain.usecase.GetEvent
import br.com.eventlist.domain.usecase.PostCheckIn
import br.com.eventlist.presenter.model.EventUiModel
import br.com.eventlist.presenter.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private lateinit var viewModel: EventDetailViewModel
    private val arg: EventDetailFragmentArgs by navArgs()
    private var name: String = ""
    private var email: String = ""
    private var eventUiModel: EventUiModel? = null
    private var _isCheckIn = false
    private lateinit var load: ConstraintLayout

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginRepository = LoginRepository()
        loginRepository.loadUser(
            requireContext(),
            requireActivity().getPreferences(Context.MODE_PRIVATE)
        )
        name = loginRepository.user?.name ?: ""
        email = loginRepository.user?.email ?: ""

        val repository = EventRepositoryImpl(
            requireActivity().getPreferences(Context.MODE_PRIVATE),
            ServiceApi().createService(EventApi::class.java)
        )
        val factory = EventDetailViewModel.Factory(
            requireActivity().getPreferences(Context.MODE_PRIVATE),
            PostCheckIn(repository), GetEvent(repository)
        )
        viewModel = ViewModelProvider(this, factory)[EventDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        binding.toolbarDetail.title = arg.title
        load = binding.load.layoutLoadAnimation
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCheckIn()

        binding.buttonCheckIn.setOnClickListener {

            viewModel.checkIn(
                requireActivity(),
                CheckIn(arg.id, name, email)
            )
        }

        binding.buttonSharing.setOnClickListener {

            SharingUtil.sharing(
                requireActivity(),
                arg.title,
                eventUiModel?.description ?: ""
            )
        }

        viewModel.checkIn.observe(viewLifecycleOwner) { resources ->

            when (resources.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    setIconCheckInButton()
                }
                Status.ERROR -> {
                    showLoginFailed(requireContext(), resources.message ?: "")
                }
            }
        }

        viewModel.event.observe(viewLifecycleOwner) { resources ->

            when (resources.status) {
                Status.LOADING -> {
                    load.show()
                }
                Status.SUCCESS -> {
                    load.hide()
                    if (resources.data != null) {
                        populateEvent(resources.data)
                        binding.buttonSharing.show()
                    }
                }
                Status.ERROR -> {
                    load.hide()
                }
            }
        }
        viewModel.getEvent(requireContext(), arg.id)
    }

    private fun isCheckIn() {
        _isCheckIn = viewModel.isCheckIn(arg.id)
        if (_isCheckIn) {
            setIconCheckInButton()
        }
    }
    private fun setIconCheckInButton() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.buttonCheckIn.setImageResource(R.drawable.ic_baseline_check_24)
        }
    }

    private fun populateEvent(eventUiModel: EventUiModel) {
        this.eventUiModel = eventUiModel
        with(binding) {
            Glide.with(requireContext())
                .load(eventUiModel.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_default)
                .into(imageTop)
            contentDetail.detailDescription.text = eventUiModel.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}