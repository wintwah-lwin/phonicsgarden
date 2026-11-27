package com.example.phonicsgarden.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Version 1 -> 2.
 *
 * - `words` is rebuilt: every word now needs an emoji picture (NOT NULL) and records its source
 *   (bundled or downloaded). The table is only a cache of learning content, so it is dropped and
 *   re-seeded on the next launch rather than copied. Children's progress tables are untouched.
 * - `attempts` gets an index on `timestamp` so the per-day statistics query stays fast.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `words`")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `words` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`text` TEXT NOT NULL, " +
                "`firstLetter` TEXT NOT NULL, " +
                "`phase` INTEGER NOT NULL, " +
                "`emoji` TEXT NOT NULL, " +
                "`source` TEXT NOT NULL)"
        )
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_words_text` ON `words` (`text`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attempts_timestamp` ON `attempts` (`timestamp`)")
    }
}
