package com.example.phonicsgarden.di

import android.content.Context
import androidx.room.Room
import com.example.phonicsgarden.data.local.AppDatabase
import com.example.phonicsgarden.data.local.MIGRATION_1_2
import com.example.phonicsgarden.data.local.dao.AttemptDao
import com.example.phonicsgarden.data.local.dao.GraphemeDao
import com.example.phonicsgarden.data.local.dao.MasteryDao
import com.example.phonicsgarden.data.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "phonics_garden.db")
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides fun provideGraphemeDao(db: AppDatabase): GraphemeDao = db.graphemeDao()
    @Provides fun provideWordDao(db: AppDatabase): WordDao = db.wordDao()
    @Provides fun provideAttemptDao(db: AppDatabase): AttemptDao = db.attemptDao()
    @Provides fun provideMasteryDao(db: AppDatabase): MasteryDao = db.masteryDao()
}
