package com.indiancalorietracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface AIAssistantRepository {
    fun sendMessage(userMessage: String, context: Map<String, Any> = emptyMap()): Flow<String>
    fun getFitnessContext(): Map<String, Any>
}