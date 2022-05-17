package br.com.eventlist.presenter.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.eventlist.R
import br.com.eventlist.domain.model.CheckIn
import br.com.eventlist.domain.model.Event
import br.com.eventlist.domain.usecase.GetEventUseCase
import br.com.eventlist.domain.usecase.PostCheckIn
import br.com.eventlist.presenter.model.toUiModel
import br.com.eventlist.presenter.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class EventDetailViewModelTest {

    private lateinit var postCheckIn: PostCheckIn
    private lateinit var getEventUseCase: GetEventUseCase
    private lateinit var eventDetailViewModel: EventDetailViewModel
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        context = Mockito.mock(Context::class.java)
        sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        postCheckIn = Mockito.mock(PostCheckIn::class.java)
        getEventUseCase = Mockito.mock(GetEventUseCase::class.java)
        eventDetailViewModel = EventDetailViewModel(sharedPreferences, postCheckIn, getEventUseCase)
    }

    @Test
    fun `get event detail`() = runTest {
        val event = Event("1", "description", "image.com.br", 10.00, "title")
        Mockito.`when`(getEventUseCase.invoke("1"))
            .thenReturn(event)
        eventDetailViewModel.getEvent(context, "1")
        assertThat(eventDetailViewModel.event.value).isNotEqualTo(Resource.success(event.toUiModel()))
    }

    @Test
    fun `post checking`() = runTest {
        Mockito.`when`(
            postCheckIn.invoke(
                CheckIn("1", "name", "email")
            )
        ).thenReturn(true)
        eventDetailViewModel.checkIn(context, CheckIn("1", "name", "email@email.com"))
        assertThat(eventDetailViewModel.checkIn.value)
            .isNotEqualTo(Resource.success(context.getString(R.string.success_checking)))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}