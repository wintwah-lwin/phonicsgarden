package com.example.phonicsgarden.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.phonicsgarden.data.local.dao.AttemptDao;
import com.example.phonicsgarden.data.local.dao.AttemptDao_Impl;
import com.example.phonicsgarden.data.local.dao.GraphemeDao;
import com.example.phonicsgarden.data.local.dao.GraphemeDao_Impl;
import com.example.phonicsgarden.data.local.dao.MasteryDao;
import com.example.phonicsgarden.data.local.dao.MasteryDao_Impl;
import com.example.phonicsgarden.data.local.dao.WordDao;
import com.example.phonicsgarden.data.local.dao.WordDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile GraphemeDao _graphemeDao;

  private volatile WordDao _wordDao;

  private volatile AttemptDao _attemptDao;

  private volatile MasteryDao _masteryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `graphemes` (`id` INTEGER NOT NULL, `letter` TEXT NOT NULL, `spokenSound` TEXT NOT NULL, `phase` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `words` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `text` TEXT NOT NULL, `firstLetter` TEXT NOT NULL, `phase` INTEGER NOT NULL, `imageUrl` TEXT)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_words_text` ON `words` (`text`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attempts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `graphemeId` INTEGER NOT NULL, `game` TEXT NOT NULL, `correct` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `mastery` (`graphemeId` INTEGER NOT NULL, `box` INTEGER NOT NULL, `nextReviewAt` INTEGER NOT NULL, PRIMARY KEY(`graphemeId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2a9d0baf4ee452df500a0f65b6d8f580')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `graphemes`");
        db.execSQL("DROP TABLE IF EXISTS `words`");
        db.execSQL("DROP TABLE IF EXISTS `attempts`");
        db.execSQL("DROP TABLE IF EXISTS `mastery`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsGraphemes = new HashMap<String, TableInfo.Column>(4);
        _columnsGraphemes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGraphemes.put("letter", new TableInfo.Column("letter", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGraphemes.put("spokenSound", new TableInfo.Column("spokenSound", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGraphemes.put("phase", new TableInfo.Column("phase", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGraphemes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGraphemes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGraphemes = new TableInfo("graphemes", _columnsGraphemes, _foreignKeysGraphemes, _indicesGraphemes);
        final TableInfo _existingGraphemes = TableInfo.read(db, "graphemes");
        if (!_infoGraphemes.equals(_existingGraphemes)) {
          return new RoomOpenHelper.ValidationResult(false, "graphemes(com.example.phonicsgarden.data.local.entity.Grapheme).\n"
                  + " Expected:\n" + _infoGraphemes + "\n"
                  + " Found:\n" + _existingGraphemes);
        }
        final HashMap<String, TableInfo.Column> _columnsWords = new HashMap<String, TableInfo.Column>(5);
        _columnsWords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("firstLetter", new TableInfo.Column("firstLetter", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("phase", new TableInfo.Column("phase", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWords = new HashSet<TableInfo.Index>(1);
        _indicesWords.add(new TableInfo.Index("index_words_text", true, Arrays.asList("text"), Arrays.asList("ASC")));
        final TableInfo _infoWords = new TableInfo("words", _columnsWords, _foreignKeysWords, _indicesWords);
        final TableInfo _existingWords = TableInfo.read(db, "words");
        if (!_infoWords.equals(_existingWords)) {
          return new RoomOpenHelper.ValidationResult(false, "words(com.example.phonicsgarden.data.local.entity.Word).\n"
                  + " Expected:\n" + _infoWords + "\n"
                  + " Found:\n" + _existingWords);
        }
        final HashMap<String, TableInfo.Column> _columnsAttempts = new HashMap<String, TableInfo.Column>(5);
        _columnsAttempts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttempts.put("graphemeId", new TableInfo.Column("graphemeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttempts.put("game", new TableInfo.Column("game", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttempts.put("correct", new TableInfo.Column("correct", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttempts.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttempts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAttempts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAttempts = new TableInfo("attempts", _columnsAttempts, _foreignKeysAttempts, _indicesAttempts);
        final TableInfo _existingAttempts = TableInfo.read(db, "attempts");
        if (!_infoAttempts.equals(_existingAttempts)) {
          return new RoomOpenHelper.ValidationResult(false, "attempts(com.example.phonicsgarden.data.local.entity.Attempt).\n"
                  + " Expected:\n" + _infoAttempts + "\n"
                  + " Found:\n" + _existingAttempts);
        }
        final HashMap<String, TableInfo.Column> _columnsMastery = new HashMap<String, TableInfo.Column>(3);
        _columnsMastery.put("graphemeId", new TableInfo.Column("graphemeId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMastery.put("box", new TableInfo.Column("box", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMastery.put("nextReviewAt", new TableInfo.Column("nextReviewAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMastery = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMastery = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMastery = new TableInfo("mastery", _columnsMastery, _foreignKeysMastery, _indicesMastery);
        final TableInfo _existingMastery = TableInfo.read(db, "mastery");
        if (!_infoMastery.equals(_existingMastery)) {
          return new RoomOpenHelper.ValidationResult(false, "mastery(com.example.phonicsgarden.data.local.entity.Mastery).\n"
                  + " Expected:\n" + _infoMastery + "\n"
                  + " Found:\n" + _existingMastery);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2a9d0baf4ee452df500a0f65b6d8f580", "033c3fd5369f1942c89f4dd6e0b4381a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "graphemes","words","attempts","mastery");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `graphemes`");
      _db.execSQL("DELETE FROM `words`");
      _db.execSQL("DELETE FROM `attempts`");
      _db.execSQL("DELETE FROM `mastery`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(GraphemeDao.class, GraphemeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WordDao.class, WordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AttemptDao.class, AttemptDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MasteryDao.class, MasteryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public GraphemeDao graphemeDao() {
    if (_graphemeDao != null) {
      return _graphemeDao;
    } else {
      synchronized(this) {
        if(_graphemeDao == null) {
          _graphemeDao = new GraphemeDao_Impl(this);
        }
        return _graphemeDao;
      }
    }
  }

  @Override
  public WordDao wordDao() {
    if (_wordDao != null) {
      return _wordDao;
    } else {
      synchronized(this) {
        if(_wordDao == null) {
          _wordDao = new WordDao_Impl(this);
        }
        return _wordDao;
      }
    }
  }

  @Override
  public AttemptDao attemptDao() {
    if (_attemptDao != null) {
      return _attemptDao;
    } else {
      synchronized(this) {
        if(_attemptDao == null) {
          _attemptDao = new AttemptDao_Impl(this);
        }
        return _attemptDao;
      }
    }
  }

  @Override
  public MasteryDao masteryDao() {
    if (_masteryDao != null) {
      return _masteryDao;
    } else {
      synchronized(this) {
        if(_masteryDao == null) {
          _masteryDao = new MasteryDao_Impl(this);
        }
        return _masteryDao;
      }
    }
  }
}
