package com.example.phonicsgarden.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.phonicsgarden.data.local.entity.Grapheme;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GraphemeDao_Impl implements GraphemeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Grapheme> __insertionAdapterOfGrapheme;

  public GraphemeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGrapheme = new EntityInsertionAdapter<Grapheme>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `graphemes` (`id`,`letter`,`spokenSound`,`phase`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grapheme entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getLetter());
        statement.bindString(3, entity.getSpokenSound());
        statement.bindLong(4, entity.getPhase());
      }
    };
  }

  @Override
  public Object insertAll(final List<Grapheme> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGrapheme.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Grapheme>> observeAll() {
    final String _sql = "SELECT * FROM graphemes ORDER BY id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"graphemes"}, new Callable<List<Grapheme>>() {
      @Override
      @NonNull
      public List<Grapheme> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLetter = CursorUtil.getColumnIndexOrThrow(_cursor, "letter");
          final int _cursorIndexOfSpokenSound = CursorUtil.getColumnIndexOrThrow(_cursor, "spokenSound");
          final int _cursorIndexOfPhase = CursorUtil.getColumnIndexOrThrow(_cursor, "phase");
          final List<Grapheme> _result = new ArrayList<Grapheme>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Grapheme _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpLetter;
            _tmpLetter = _cursor.getString(_cursorIndexOfLetter);
            final String _tmpSpokenSound;
            _tmpSpokenSound = _cursor.getString(_cursorIndexOfSpokenSound);
            final int _tmpPhase;
            _tmpPhase = _cursor.getInt(_cursorIndexOfPhase);
            _item = new Grapheme(_tmpId,_tmpLetter,_tmpSpokenSound,_tmpPhase);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUpToPhase(final int phase,
      final Continuation<? super List<Grapheme>> $completion) {
    final String _sql = "SELECT * FROM graphemes WHERE phase <= ? ORDER BY id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, phase);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Grapheme>>() {
      @Override
      @NonNull
      public List<Grapheme> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLetter = CursorUtil.getColumnIndexOrThrow(_cursor, "letter");
          final int _cursorIndexOfSpokenSound = CursorUtil.getColumnIndexOrThrow(_cursor, "spokenSound");
          final int _cursorIndexOfPhase = CursorUtil.getColumnIndexOrThrow(_cursor, "phase");
          final List<Grapheme> _result = new ArrayList<Grapheme>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Grapheme _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpLetter;
            _tmpLetter = _cursor.getString(_cursorIndexOfLetter);
            final String _tmpSpokenSound;
            _tmpSpokenSound = _cursor.getString(_cursorIndexOfSpokenSound);
            final int _tmpPhase;
            _tmpPhase = _cursor.getInt(_cursorIndexOfPhase);
            _item = new Grapheme(_tmpId,_tmpLetter,_tmpSpokenSound,_tmpPhase);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM graphemes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
