package com.example.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserProfile
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val userProfile: UserProfile? = null,
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val uiState: StateFlow<ProfileUiState> = combine(
        authRepository.currentUser,
        authRepository.isUserSignedIn,
        _isLoading,
        _errorMessage
    ) { user, signedIn, loading, error ->
        ProfileUiState(
            isLoading = loading,
            isSignedIn = signedIn,
            userProfile = user,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun signInWithGoogleAccount(
        email: String,
        displayName: String,
        onRequiresProfileCompletion: () -> Unit,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = authRepository.signInWithGoogleAccount(email, displayName)
            _isLoading.value = false
            result.onSuccess { profile ->
                if (!profile.isProfileComplete) {
                    onRequiresProfileCompletion()
                } else {
                    onComplete()
                }
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Google Sign-In failed"
            }
        }
    }

    fun signInWithGoogle(onRequiresProfileCompletion: () -> Unit, onComplete: () -> Unit) {
        signInWithGoogleAccount(
            email = "tharunprabashwara642@gmail.com",
            displayName = "Tharun Prabashwara",
            onRequiresProfileCompletion = onRequiresProfileCompletion,
            onComplete = onComplete
        )
    }

    fun completeProfile(
        fullName: String,
        phone: String,
        location: String,
        categories: List<String>,
        headline: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = authRepository.completeProfile(fullName, phone, location, categories, headline)
            _isLoading.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Failed to save profile"
            }
        }
    }

    fun updateProfile(profile: UserProfile, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = authRepository.updateProfile(profile)
            _isLoading.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Failed to update profile"
            }
        }
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            authRepository.signOut()
            _isLoading.value = false
            onSignedOut()
        }
    }
}
