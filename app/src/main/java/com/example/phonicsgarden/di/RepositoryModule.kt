package com.example.phonicsgarden.di

import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.ProgressRepositoryImpl
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.data.repository.WordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Binds repository interfaces to implementations so ViewModels can be tested with fakes. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindWordRepository(impl: WordRepositoryImpl): WordRepository

    @Binds @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository
}
