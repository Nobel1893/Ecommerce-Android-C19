package com.route.firstapp.ui.session

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

// Decides where the app starts: Home when a login is saved on the device, Login otherwise.
interface SessionContract {
    abstract class ViewModel : androidx.lifecycle.ViewModel(){
        abstract val states : StateFlow<State>
        abstract val events : Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    sealed class State{
        data object Loading: State()
        data object LoggedIn: State()
        // also used when the saved session can't be read: the user signs in again
        data object LoggedOut: State()
    }
    sealed class Intent{
        data object Init: Intent()
    }
    sealed class Event{
    }
}
