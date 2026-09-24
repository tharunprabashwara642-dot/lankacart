package com.example.presentation.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AdStatus
import com.example.domain.model.Advertisement
import com.example.domain.model.AdvertisementPackage
import com.example.domain.model.UserProfile
import com.example.domain.repository.AdvertisementRepository
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AdsUiState(
    val isLoading: Boolean = true,
    val approvedAds: List<Advertisement> = emptyList(),
    val suggestedAds: List<Advertisement> = emptyList(),
    val userAds: List<Advertisement> = emptyList(),
    val userProfile: UserProfile? = null,
    val packages: List<AdvertisementPackage> = emptyList(),
    val isSubmitting: Boolean = false,
    val submissionSuccess: Boolean = false,
    val errorMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class AdsViewModel(
    private val advertisementRepository: AdvertisementRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _submissionSuccess = MutableStateFlow(false)
    val submissionSuccess: StateFlow<Boolean> = _submissionSuccess.asStateFlow()

    private val suggestedFlow = authRepository.currentUser.flatMapLatest { user ->
        val cats = user?.preferredCategories ?: emptyList()
        val loc = user?.preferredLocation ?: ""
        advertisementRepository.getSuggestedAdvertisements(cats, loc)
    }

    val uiState: StateFlow<AdsUiState> = combine(
        advertisementRepository.getApprovedAdvertisements(),
        suggestedFlow,
        authRepository.currentUser,
        _isSubmitting,
        _submissionSuccess
    ) { allAds, suggested, user, submitting, success ->
        val userAdsList = if (user != null) {
            allAds.filter { it.userId == user.id }
        } else {
            emptyList()
        }
        AdsUiState(
            isLoading = false,
            approvedAds = allAds,
            suggestedAds = suggested,
            userAds = userAdsList,
            userProfile = user,
            packages = advertisementRepository.getPackages(),
            isSubmitting = submitting,
            submissionSuccess = success,
            errorMessage = _errorMessage.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdsUiState(isLoading = true, packages = advertisementRepository.getPackages())
    )

    fun submitAdvertisement(
        title: String,
        organizationName: String,
        description: String,
        category: String,
        location: String,
        phone: String?,
        email: String?,
        websiteUrl: String?,
        selectedPackage: AdvertisementPackage,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _errorMessage.value = null

            val newAd = Advertisement(
                id = "",
                title = title.trim(),
                organizationName = organizationName.trim(),
                description = description.trim(),
                category = category.trim(),
                location = location.trim(),
                contactPhone = phone?.trim()?.ifBlank { null },
                contactEmail = email?.trim()?.ifBlank { null },
                websiteUrl = websiteUrl?.trim()?.ifBlank { null },
                packageId = selectedPackage.id,
                packageName = selectedPackage.name,
                status = AdStatus.PENDING_APPROVAL,
                createdAt = System.currentTimeMillis(),
                validUntil = System.currentTimeMillis() + (selectedPackage.durationDays * 86400000L),
                userId = "usr_google_lk_982"
            )

            val result = advertisementRepository.submitAdvertisement(newAd)
            _isSubmitting.value = false
            result.onSuccess { id ->
                _submissionSuccess.value = true
                onSuccess(id)
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Failed to submit advertisement"
            }
        }
    }

    fun resetSubmissionState() {
        _submissionSuccess.value = false
        _errorMessage.value = null
    }
}
