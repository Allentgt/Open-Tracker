package com.indiancalorietracker.ui.screens.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indiancalorietracker.domain.repository.AIAssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val isFromUser: Boolean,
    val isStreaming: Boolean = false
)

data class AIChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            content = "Hi! I'm your fitness assistant. Ask me about workouts, nutrition, or healthy habits!",
            isFromUser = false
        )
    ),
    val error: String? = null
)

@HiltViewModel
class AIChatViewModel @Inject constructor(
    private val aiAssistantRepository: AIAssistantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AIChatUiState())
    val uiState: StateFlow<AIChatUiState> = _uiState.asStateFlow()

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        // Add user message
        val userMessage = ChatMessage(content = message, isFromUser = true)
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isLoading = true,
            error = null
        )

        // Get context (could include user goals, today's meals, etc.)
        val context = aiAssistantRepository.getFitnessContext()

        viewModelScope.launch {
            aiAssistantRepository.sendMessage(message, context)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to get response"
                    )
                }
                .collect { response ->
                    // Add assistant response
                    val assistantMessage = ChatMessage(
                        content = response,
                        isFromUser = false,
                        isStreaming = false
                    )
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + assistantMessage,
                        isLoading = false
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}