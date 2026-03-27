package com.indiancalorietracker.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.indiancalorietracker.service.AppUserProfile
import com.indiancalorietracker.service.Auth0Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth0Service: Auth0Service
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val user = auth0Service.getCurrentUser()
        _authState.value = _authState.value.copy(
            userName = user?.name ?: user?.email,
            userEmail = user?.email,
            isAuthenticated = auth0Service.isAuthenticated()
        )
    }

    // Public method to refresh auth state (called when screen resumes)
    fun checkAuthState() {
        checkCurrentUser()
    }

    fun onLoginError(message: String) {
        _authState.value = _authState.value.copy(
            isLoading = false,
            error = message,
            isAuthenticated = false
        )
    }

    fun setLoading(loading: Boolean) {
        _authState.value = _authState.value.copy(isLoading = loading)
    }

    fun logout(activity: android.app.Activity) {
        _authState.value = _authState.value.copy(isLoading = true)
        auth0Service.logout(
            activity = activity,
            onSuccess = {
                _authState.value = AuthState()
            },
            onFailure = { e ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        )
    }

    fun login(activity: android.app.Activity) {
        _authState.value = _authState.value.copy(isLoading = true)
        auth0Service.login(
            activity = activity,
            onSuccess = { creds, name ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    userName = name ?: "User",
                    userEmail = creds.idToken,
                    isAuthenticated = true,
                    error = null
                )
            },
            onFailure = { e ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Login failed",
                    isAuthenticated = false
                )
            }
        )
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}