package br.com.eventlist.presenter.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import br.com.eventlist.R
import br.com.eventlist.domain.model.CheckIn
import br.com.eventlist.domain.usecase.GetEventUseCase
import br.com.eventlist.domain.usecase.PostCheckIn
import br.com.eventlist.presenter.model.EventUiModel
import br.com.eventlist.presenter.model.toUiModel
import br.com.eventlist.presenter.util.Resource
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val sharedPref: SharedPreferences,
    private val postCheckIn: PostCheckIn,
    private val getEventUseCase: GetEventUseCase
) : ViewModel() {

    private val _event = MutableLiveData<Resource<EventUiModel>>()
    val event = _event as LiveData<Resource<EventUiModel>>

    private val _checkIn= MutableLiveData<Resource<String>>()
    val checkIn: LiveData<Resource<String>> = _checkIn

    fun getEvent(context: Context, id: String) {
        _event.postValue(Resource.loading(null))
        try {
            viewModelScope.launch {
                val event = getEventUseCase(id)
                if (event != null) {
                    _event.postValue(
                        Resource.success(event.toUiModel())
                    )
                } else {
                    _event.postValue(
                        Resource.error(
                            context.getString(R.string.not_found), null
                        )
                    )
                }
            }
        } catch (e: Exception) {
            _event.postValue(
                Resource.error(
                    context.getString(R.string.unexpected_failure), null
                )
            )
        }
    }

    fun checkIn(context: Context, checkIn: CheckIn) {
        _checkIn.postValue(Resource.loading(null))
        try {
            viewModelScope.launch {
                val success = postCheckIn.invoke(checkIn)
                if (success) {
                    _checkIn.postValue(
                        Resource.success(
                            context.getString(R.string.success_checking)
                        )
                    )
                } else {
                    _checkIn.postValue(
                        Resource.error(
                            context.getString(R.string.failure_checking),
                            null
                        )
                    )
                }
            }
        } catch (e: Exception) {
            _checkIn.postValue(
                Resource.error(
                    context.getString(R.string.failure_checking),
                    null
                )
            )
        }
    }

    fun isCheckIn(eventId: String): Boolean {
        return try {
            val json = sharedPref.getString(eventId, null)
            json != null
        } catch (e: Exception) { false }
    }

    class Factory(
        private val sharedPref: SharedPreferences,
        private val postCheckIn: PostCheckIn,
        private val getEventUseCase: GetEventUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventDetailViewModel(sharedPref, postCheckIn, getEventUseCase) as T
            }
            throw IllegalArgumentException("Classe ViewModel inválida")
        }
    }
}
