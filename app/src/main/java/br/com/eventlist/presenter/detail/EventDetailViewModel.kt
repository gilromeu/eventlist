package br.com.eventlist.presenter.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import br.com.eventlist.R
import br.com.eventlist.data.repository.LoginRepository
import br.com.eventlist.domain.model.CheckIn
import br.com.eventlist.domain.usecase.GetEvent
import br.com.eventlist.domain.usecase.PostCheckIn
import br.com.eventlist.presenter.model.EventUiModel
import br.com.eventlist.presenter.model.toUiModel
import br.com.eventlist.presenter.util.Resource
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val loginRepository: LoginRepository,
    private val sharedPref: SharedPreferences,
    private val postCheckIn: PostCheckIn,
    private val getEvent: GetEvent
) : ViewModel() {

    private val _event = MutableLiveData<Resource<EventUiModel>>()
    val event = _event as LiveData<Resource<EventUiModel>>

    private val _checkIn= MutableLiveData<Resource<String>>()
    val checkIn: LiveData<Resource<String>> = _checkIn

    init {
        loginRepository.loadUser()
    }

    fun getEvent(context: Context, id: String) {
        _event.postValue(Resource.loading(null))
        try {
            viewModelScope.launch {
                val event = getEvent(id)
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

    fun checkIn(context: Context, eventId: String) {
        _checkIn.postValue(Resource.loading(null))
        try {
            viewModelScope.launch {
                val checkIn = CheckIn(
                    eventId,
                    loginRepository.user?.name ?: "",
                    loginRepository.user?.email ?: ""
                )
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
}
