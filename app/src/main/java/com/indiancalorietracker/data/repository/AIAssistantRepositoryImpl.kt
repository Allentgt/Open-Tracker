package com.indiancalorietracker.data.repository

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.indiancalorietracker.domain.repository.AIAssistantRepository
import com.indiancalorietracker.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AIAssistantRepositoryImpl @Inject constructor() : AIAssistantRepository {

    companion object {
        private const val TAG = "AIAssistantRepository"
    }

    private val apiKey: String by lazy {
        BuildConfig.GEMINI_API_KEY
    }

    init {
        Log.d(TAG, "Initializing AI Assistant with API key: ${if (apiKey.isNotEmpty()) "present (${apiKey.length} chars)" else "EMPTY"}")
    }

    private val fitnessSystemPrompt = """
You are a fitness and nutrition assistant specializing in healthy lifestyle advice. 

Your expertise includes:
- Workout planning and exercise guidance
- Nutrition and calorie management
- Hydration and wellness tips
- Weight management strategies
- Building healthy habits

Guidelines:
1. Always consider user's fitness level when giving advice
2. Provide practical, actionable tips
3. Include safety considerations for exercises
4. Be encouraging and supportive
5. When providing workout recommendations, include sets, reps, and rest periods
6. For nutrition questions, consider Indian food context since this is an Indian calorie tracker app
7. Keep responses concise but informative
8. If users ask about medical conditions, remind them to consult healthcare professionals

Remember to be helpful, accurate, and motivating!
    """.trimIndent()

    private val generativeModel: GenerativeModel by lazy {
        Log.d(TAG, "Creating GenerativeModel with key prefix: ${apiKey.take(10)}...")
        GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = apiKey
        )
    }

    override fun sendMessage(userMessage: String, context: Map<String, Any>): Flow<String> = flow {
        try {
            Log.d(TAG, "Sending message to AI: $userMessage")
            
            val contextInfo = buildContextString(context)
            val fullPrompt = "$fitnessSystemPrompt\n\n$contextInfo\n\nUser: $userMessage\n\nAssistant:"
            
            val response = generativeModel.generateContent(fullPrompt)
            val responseText = response.text
            
            Log.d(TAG, "AI Response received: ${responseText?.take(100)}...")
            emit(responseText ?: "I'm sorry, I couldn't generate a response. Please try again.")
        } catch (e: Exception) {
            Log.e(TAG, "AI API Error: ${e.message}", e)
            val fallbackResponse = when {
                userMessage.contains("workout", ignoreCase = true) -> 
                    "I'd be happy to help with your workout! To give you the best advice, please tell me:\n• Your fitness level (beginner, intermediate, advanced)\n• What equipment you have available\n• Your specific goals (strength, weight loss, muscle gain)"
                userMessage.contains("diet", ignoreCase = true) || userMessage.contains("nutrition", ignoreCase = true) ->
                    "For personalized nutrition advice, I'd like to know:\n• Your daily calorie goal\n• Your dietary preferences (vegetarian, non-veg, etc.)\n• Any specific health goals"
                userMessage.contains("weight", ignoreCase = true) ->
                    "For weight management, focus on:\n• Calorie deficit (consume less than you burn)\n• Regular exercise (both cardio and strength)\n• Consistent sleep and hydration"
                else ->
                    "I'm here to help with your fitness journey! Please ask me about:\n• Workouts and exercises\n• Nutrition and diet\n• Healthy habits\n• Weight management"
            }
            emit(fallbackResponse)
        }
    }.flowOn(Dispatchers.IO)

    private fun buildContextString(context: Map<String, Any>): String {
        if (context.isEmpty()) return ""
        
        return buildString {
            appendLine("Additional context:")
            context.forEach { (key, value) ->
                appendLine("- $key: $value")
            }
        }
    }

    override fun getFitnessContext(): Map<String, Any> {
        return emptyMap()
    }
}