package com.example.linguatale.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguatale.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    sealed class AuthState {
        object Idle: AuthState()
        object Loading: AuthState()
        object Success: AuthState()
        object NeedsConfirmation: AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            authRepository.login(email, password)
                .onSuccess { _state.value = AuthState.Success }
                .onFailure { _state.value = AuthState.Error(friendlyMessage(it)) }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            authRepository.register(email, password)
                .onSuccess { _state.value = AuthState.NeedsConfirmation }
                .onFailure { _state.value = AuthState.Error(friendlyMessage(it)) }
        }
    }

    fun confirmCode(email: String, code: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            authRepository.confirmRegistration(email, code)
                .onSuccess { _state.value = AuthState.Success }
                .onFailure { _state.value = AuthState.Error(friendlyMessage(it)) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.value = AuthState.Idle
        }
    }

    fun resetState() {
        _state.value = AuthState.Idle
    }

    private fun friendlyMessage(e: Throwable): String = when {
        e.message?.contains("UserNotFoundException") == true     -> "No account found with this email"
        e.message?.contains("NotAuthorizedException") == true    -> "Wrong email or password"
        e.message?.contains("UsernameExistsException") == true   -> "An account with this email already exists"
        e.message?.contains("InvalidPasswordException") == true  -> "Password must be at least 8 characters"
        e.message?.contains("CodeMismatchException") == true     -> "Wrong confirmation code"
        e.message?.contains("ExpiredCodeException") == true      -> "Code expired — request a new one"
        e.message?.contains("UserNotConfirmedException") == true -> "Please confirm your email first"
        else -> e.message ?: "Something went wrong"
    }
}