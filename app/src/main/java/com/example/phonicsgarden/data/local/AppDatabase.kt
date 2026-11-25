package com.example.phonicsgarden.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.phonicsgarden.data.local.dao.AttemptDao
import com.example.phonicsgarden.data.local.dao.GraphemeDao
import com.example.phonicsgarden.data.local.dao.MasteryDao
import com.example.phonicsgarden.data.local.dao.WordDao
import com.example.phonicsgarden.data.local.entity.Attempt
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Mastery
import com.example.phonicsgarden.data.local.entity.Word

@Database(
    entities = [Grapheme::class, Word::class, Attempt::class, Mastery::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun graphemeDao(): GraphemeDao
    abstract fun wordDao(): WordDao
    abstract fun attemptDao(): AttemptDao
    abstract fun masteryDao(): MasteryDao
}
