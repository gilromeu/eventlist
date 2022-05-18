package br.com.eventlist.presenter.list

import android.content.Context
import androidx.lifecycle.*
import br.com.eventlist.R
import br.com.eventlist.domain.usecase.GetEvents
import br.com.eventlist.presenter.model.EventUiModel
import br.com.eventlist.presenter.model.toUiModel
import br.com.eventlist.presenter.util.Resource
import kotlinx.coroutines.launch

class EventsViewModel(
    private val getEvents: GetEvents
) : ViewModel() {

    private val _events = MutableLiveData<Resource<List<EventUiModel>>>()
    val events = _events as LiveData<Resource<List<EventUiModel>>>

    fun getEvents(context: Context) {
        _events.postValue(Resource.loading(null))
        try {
            viewModelScope.launch {
                val eventList = getEvents()

                _events.postValue(
                    Resource.success(eventList.toUiModel())
                )
            }
        } catch (e: Exception) {
            _events.postValue(
                Resource.error(
                    context.getString(R.string.unexpected_failure), listOf()
                )
            )
        }
    }
}