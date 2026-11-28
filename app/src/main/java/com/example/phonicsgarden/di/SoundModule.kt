package com.example.phonicsgarden.di

import com.example.phonicsgarden.util.SoundEffects
import com.example.phonicsgarden.util.SoundPoolEffects
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SoundModule {
    @Binds @Singleton
    abstract fun bindSoundEffects(impl: SoundPoolEffects): SoundEffects
}
