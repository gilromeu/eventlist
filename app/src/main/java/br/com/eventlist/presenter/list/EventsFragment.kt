package br.com.eventlist.presenter.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import br.com.eventlist.data.repository.EventRepositoryImpl
import br.com.eventlist.data.api.EventApi
import br.com.eventlist.data.api.ServiceApi
import br.com.eventlist.databinding.FragmentEventsBinding
import br.com.eventlist.domain.usecase.GetEvents
import br.com.eventlist.presenter.adapter.EventsAdapter
import br.com.eventlist.presenter.model.EventUiModel
import br.com.eventlist.presenter.util.Status
import br.com.eventlist.presenter.util.hide
import br.com.eventlist.presenter.util.show

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private lateinit var viewModel: EventsViewModel
    private lateinit var load: ConstraintLayout
    private val binding get() = _binding!!
    private lateinit var messageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = EventsViewModel.Factory(
            GetEvents(
                EventRepositoryImpl(
                    requireActivity().getPreferences(Context.MODE_PRIVATE),
                    ServiceApi().createService(EventApi::class.java)
                )
            )
        )
        viewModel = ViewModelProvider(this, factory)[EventsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        load = binding.load.layoutLoadAnimation
        messageTextView = binding.contentEvents.messageEvents
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.events.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    load.show()
                }
                Status.SUCCESS -> {
                    load.hide()
                    if (resources.data != null && resources.data.isNotEmpty()) {
                        setAdapter(resources.data)
                    } else {
                        messageTextView.show()
                    }
                }
                Status.ERROR -> {
                    load.hide()
                    messageTextView.show()
                }
            }
        }
        viewModel.getEvents(requireContext())
    }


    private fun setAdapter(list: List<EventUiModel>) {
        binding.contentEvents.eventRecyclerView.adapter = EventsAdapter(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}