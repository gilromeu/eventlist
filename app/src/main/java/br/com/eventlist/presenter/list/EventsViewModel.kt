package br.com.eventlist.presenter.list

import android.content.Context
import androidx.lifecycle.*
import br.com.eventlist.R
import br.com.eventlist.domain.usecase.GetEventsUseCase
import br.com.eventlist.presenter.model.EventUiModel
import br.com.eventlist.presenter.model.toUiModel
import br.com.eventlist.presenter.util.Resource
import kotlinx.coroutines.launch

class EventsViewModel(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _events = MutableLiveData<Resource<List<EventUiModel>>>()
    val events = _events as LiveData<Resource<List<EventUiModel>>>

    fun getEvents(context: Context) {
        _events.postValue(Resource.loading(null))
        try {
            viewModelScope.launch {
                val eventList = getEventsUseCase()

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

    class Factory(
        private val getEventsUseCase: GetEventsUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventsViewModel(getEventsUseCase) as T
            }
            throw IllegalArgumentException("Classe ViewModel inválida")
        }
    }
}