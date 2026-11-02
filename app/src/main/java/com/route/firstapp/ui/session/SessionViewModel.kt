package com.route.firstapp.ui.session

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Resource
import com.route.domain.usecase.IsLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val isLoggedInUseCase: IsLoggedInUseCase,
) : SessionContract.ViewModel(){

    private val _events = Channel<SessionContract.Event>(Channel.BUFFERED)
    override val events: Flow<SessionContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<SessionContract.State>(SessionContract.State.Loading)
    override val states: StateFlow<SessionContract.State> = _states.asStateFlow()

    private var sessionJob: Job? = null

    override fun doIntent(intent: SessionContract.Intent) {
        when (intent) {
            SessionContract.Intent.Init -> {
                // Init comes again after rotation: the start destination is decided once, or the NavHost would restart
                if (sessionJob == null) loadSession()
            }
        }
    }

    private fun loadSession(){
        sessionJob = viewModelScope.launch {
            isLoggedInUseCase.isLoggedIn()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _states.emit(SessionContract.State.Loading)
                        }
                        is Resource.Success -> {
                            _states.emit(
                                if (resource.data == true) SessionContract.State.LoggedIn
                                else SessionContract.State.LoggedOut
                            )
                        }
                        is Resource.Error -> {
                            _states.emit(SessionContract.State.LoggedOut)
                        }
                    }
                }
        }
    }
}
