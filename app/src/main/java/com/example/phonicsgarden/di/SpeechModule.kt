package com.example.phonicsgarden.di

import com.example.phonicsgarden.util.Speaker
import com.example.phonicsgarden.util.TtsSpeaker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpeechModule {
    @Binds @Singleton
    abstract fun bindSpeaker(impl: TtsSpeaker): Speaker
}
