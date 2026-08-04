package com.example.linguatale.di

import android.content.Context
import com.example.linguatale.data.local.datastore.TokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideTokenDataStore(@ApplicationContext ctx: Context): TokenDataStore =
        TokenDataStore(ctx)
}