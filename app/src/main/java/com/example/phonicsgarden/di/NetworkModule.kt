package com.example.phonicsgarden.di

import com.example.phonicsgarden.data.remote.DatamuseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.datamuse.com/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideDatamuseApi(retrofit: Retrofit): DatamuseApi =
        retrofit.create(DatamuseApi::class.java)
}
