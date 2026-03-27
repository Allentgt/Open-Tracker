package com.indiancalorietracker.di

import android.content.Context
import com.indiancalorietracker.service.Auth0Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuth0Service(
        @ApplicationContext context: Context
    ): Auth0Service {
        return Auth0Service(context)
    }
}