package br.com.eventlist.data.di

import br.com.eventlist.presenter.detail.EventDetailViewModel
import br.com.eventlist.presenter.list.EventsViewModel
import br.com.eventlist.presenter.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { EventsViewModel(get()) }

    viewModel { EventDetailViewModel(get(), get(), get(), get()) }

    viewModel { LoginViewModel(get()) }
}