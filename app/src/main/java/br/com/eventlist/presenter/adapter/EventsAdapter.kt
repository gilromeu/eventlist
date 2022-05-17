package br.com.eventlist.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.com.eventlist.R
import br.com.eventlist.databinding.ItemEventBinding
import br.com.eventlist.presenter.list.EventsFragmentDirections
import br.com.eventlist.presenter.model.EventUiModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class EventsAdapter(
    private val events: List<EventUiModel>
) : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        if (events.isNotEmpty()) {
            val event = events[position]

            with(holder) {
                binding.eventTitle.text = event.title
                binding.eventPrice.text = event.price
                Glide.with(holder.itemView.context)
                    .load(event.image.trim())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.image_default)
                    .into(binding.eventImage)
                binding.card.setOnClickListener { view ->
                    val direction = EventsFragmentDirections
                        .actionEventsFragmentToDetailFragment(event.id, event.title)
                    view.findNavController().navigate(direction)
                }
            }
        }
    }

    override fun getItemCount() = events.size

    inner class EventsViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)
}