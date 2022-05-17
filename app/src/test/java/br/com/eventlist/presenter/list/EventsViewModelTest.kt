package br.com.eventlist.presenter.list

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.eventlist.domain.model.Event
import br.com.eventlist.domain.usecase.GetEventsUseCase
import br.com.eventlist.presenter.util.Resource
import com.google.common.truth.Truth
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
class EventsViewModelTest {

    private lateinit var getEventsUseCase: GetEventsUseCase
    private lateinit var eventsViewModel: EventsViewModel
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
        getEventsUseCase = Mockito.mock(GetEventsUseCase::class.java)
        eventsViewModel = EventsViewModel(getEventsUseCase)
    }

    @Test
    fun `get event list`() = runTest {
        val list = listOf(
            Event("1", "description", "image.jpg", 10.00, "title"),
            Event("2", "description", "image.jpg", 10.00, "title")
        )
        Mockito.`when`(
            getEventsUseCase.invoke()
        ).thenReturn(list)
        eventsViewModel.getEvents(context)
        Truth.assertThat(eventsViewModel.events.value)
            .isEqualTo(Resource.loading(null))
        Truth.assertThat(eventsViewModel.events.value)
            .isEqualTo(Resource.success(list))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}