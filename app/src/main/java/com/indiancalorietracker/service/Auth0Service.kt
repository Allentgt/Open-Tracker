package com.indiancalorietracker.service

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Auth0Service @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val auth0: Auth0 by lazy {
        Auth0(
            "04QlYhAF7SUClIYeSMsu7Q2gHC2ePSkQ",
            "dev-allentgt.us.auth0.com"
        )
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("auth0_prefs", Context.MODE_PRIVATE)
    }

    // Initialize credentials from storage on creation
    init {
        loadCredentialsFromStorage()
    }

    private fun loadCredentialsFromStorage() {
        val token = prefs.getString("access_token", null)
        val expiresAtMillis = prefs.getLong("expires_at", 0)
        val idToken = prefs.getString("id_token", null)
        val refreshToken = prefs.getString("refresh_token", null)
        
        if (token != null && expiresAtMillis > System.currentTimeMillis()) {
            currentCredentials = Credentials(
                accessToken = token,
                idToken = idToken ?: "",
                refreshToken = refreshToken,
                expiresAt = Date(expiresAtMillis),
                type = "Bearer",
                scope = "openid profile email"
            )
        }
    }

    private fun saveCredentialsToStorage(credentials: Credentials) {
        prefs.edit().apply {
            putString("access_token", credentials.accessToken)
            credentials.idToken?.let { putString("id_token", it) }
            credentials.refreshToken?.let { putString("refresh_token", it) }
            credentials.expiresAt?.time?.let { putLong("expires_at", it) }
            apply()
        }
    }

    private fun clearCredentialsFromStorage() {
        prefs.edit().clear().apply()
    }

    private fun saveUserNameToStorage(name: String, email: String?) {
        prefs.edit().apply {
            putString("user_name", name)
            email?.let { putString("user_email", it) }
            apply()
        }
        currentUserProfile = AppUserProfile(name = name, email = email)
    }

    private fun loadUserNameFromStorage(): AppUserProfile? {
        val name = prefs.getString("user_name", null)
        val email = prefs.getString("user_email", null)
        return if (name != null) AppUserProfile(name = name, email = email) else null
    }

    fun login(activity: Activity, onSuccess: (Credentials, String?) -> Unit, onFailure: (Exception) -> Unit) {
        WebAuthProvider.login(auth0)
            .withScheme("com.indiancalorietracker")
            .withAudience("https://dev-allentgt.us.auth0.com/api/v2/")
            .start(activity, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    onFailure(exception)
                }

                override fun onSuccess(credentials: Credentials) {
                    currentCredentials = credentials
                    saveCredentialsToStorage(credentials)
                    
                    // Try to decode ID token to get user info
                    val name = try {
                        decodeJwtName(credentials.idToken)
                    } catch (e: Exception) {
                        null
                    }
                    
                    val displayName = name ?: "User"
                    saveUserNameToStorage(displayName, name)
                    onSuccess(credentials, displayName)
                }
            })
    }
    
    // Simple JWT decoding to extract name from ID token
    private fun decodeJwtName(jwt: String?): String? {
        if (jwt == null) return null
        try {
            val parts = jwt.split(".")
            if (parts.size < 2) return null
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT))
            // Simple extraction of name from JSON
            val nameMatch = Regex("\"name\"\\s*:\\s*\"([^\"]+)\"").find(payload)
            if (nameMatch != null) return nameMatch.groupValues[1]
            val emailMatch = Regex("\"email\"\\s*:\\s*\"([^\"]+)\"").find(payload)
            return emailMatch?.groupValues?.get(1)
        } catch (e: Exception) {
            return null
        }
    }

    fun logout(activity: Activity, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        WebAuthProvider.logout(auth0)
            .withScheme("com.indiancalorietracker")
            .start(activity, object : Callback<Void?, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    onFailure(exception)
                }

                override fun onSuccess(payload: Void?) {
                    currentCredentials = null
                    currentUserProfile = null
                    clearCredentialsFromStorage()
                    // Clear user name
                    prefs.edit().remove("user_name").remove("user_email").apply()
                    onSuccess()
                }
            })
    }

    fun isAuthenticated(): Boolean {
        return currentCredentials != null && 
               currentCredentials?.expiresAt?.time?.let { it > System.currentTimeMillis() } == true
    }

    fun getCurrentUser(): AppUserProfile? {
        if (!isAuthenticated()) return null
        return currentUserProfile ?: loadUserNameFromStorage()
    }

    // Call this when app resumes to check if user just logged in
    fun refreshAuthState() {
        loadCredentialsFromStorage()
        currentUserProfile = loadUserNameFromStorage()
    }

    companion object {
        @Volatile
        var currentCredentials: Credentials? = null
        var currentUserProfile: AppUserProfile? = null
    }
}

data class AppUserProfile(
    val name: String? = null,
    val email: String? = null,
    val picture: String? = null
)
