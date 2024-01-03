package com.feature.profile.impl.ui.auth.signin

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.profile.api.model.User
import com.feature.profile.impl.mapper.UserMapper
import com.itis.core.db.DatabaseHandler
import com.itis.core.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class SignInViewState(
    val messageState: String = "",
    val idState: Long = -1L
)

sealed interface SignInEvent {
    data class OnSignInClick(val username: String, val password: String): SignInEvent
    object OnSignUpNavigateClick: SignInEvent
    object OnNavigateProfile: SignInEvent
}

sealed interface SignInAction {
    object NavigateSignUp: SignInAction
    object NavigateProfile: SignInAction
}

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SignInViewState())
    val state: StateFlow<SignInViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<SignInAction?>()
    val action: SharedFlow<SignInAction?>
        get() = _action.asSharedFlow()

    fun event(event: SignInEvent) {
        when (event) {
            SignInEvent.OnSignUpNavigateClick -> onNavigateSignUp()
            SignInEvent.OnNavigateProfile -> onNavigateProfile()
            is SignInEvent.OnSignInClick -> onSignInClick(event)
        }
    }

    private fun onSignInClick(event: SignInEvent.OnSignInClick) {
        viewModelScope.launch {
            val user: User? = UserMapper.mapUserModel(DatabaseHandler.getUser(event.username, event.password))
            if (user != null) {
                _state.emit(_state.value.copy(idState = user.id))
            } else {
                _state.emit(_state.value.copy(messageState = "incorrect entered login/password"))
            }
        }
    }

    private fun onNavigateSignUp() {
        viewModelScope.launch {
            _action.emit(SignInAction.NavigateSignUp)
        }
    }

    private fun onNavigateProfile() {
        viewModelScope.launch {
            _action.emit(SignInAction.NavigateProfile)
        }
    }
}