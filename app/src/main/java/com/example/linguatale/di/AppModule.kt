package com.example.linguatale.di

import android.content.Context
import androidx.room.Room
import com.example.linguatale.data.local.datastore.TokenDataStore
import com.example.linguatale.data.remote.api.BookApi
import com.example.linguatale.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideTokenDataStore(@ApplicationContext ctx: Context): TokenDataStore =
        TokenDataStore(ctx)

    @Provides @Singleton
    fun provideOkHttpClient(tokenDataStore: TokenDataStore): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(
                    tokenDataStore,
                    CoroutineScope(Dispatchers.IO)
                )
            )
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)  // set in build.gradle per flavor
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideBookApi(retrofit: Retrofit): BookApi =
        retrofit.create(BookApi::class.java)

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "bookapp.db")
            .fallbackToDestructiveMigration()
            .build()
}