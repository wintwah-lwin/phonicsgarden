package com.example.phonicsgarden.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.phonicsgarden.data.local.entity.Mastery;
import java.lang.Class;
import java.lang.Exception;
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
public final class MasteryDao_Impl implements MasteryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Mastery> __insertionAdapterOfMastery;

  public MasteryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMastery = new EntityInsertionAdapter<Mastery>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mastery` (`graphemeId`,`box`,`nextReviewAt`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Mastery entity) {
        statement.bindLong(1, entity.getGraphemeId());
        statement.bindLong(2, entity.getBox());
        statement.bindLong(3, entity.getNextReviewAt());
      }
    };
  }

  @Override
  public Object upsert(final Mastery mastery, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMastery.insert(mastery);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Mastery>> observeAll() {
    final String _sql = "SELECT * FROM mastery";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"mastery"}, new Callable<List<Mastery>>() {
      @Override
      @NonNull
      public List<Mastery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGraphemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "graphemeId");
          final int _cursorIndexOfBox = CursorUtil.getColumnIndexOrThrow(_cursor, "box");
          final int _cursorIndexOfNextReviewAt = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewAt");
          final List<Mastery> _result = new ArrayList<Mastery>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Mastery _item;
            final long _tmpGraphemeId;
            _tmpGraphemeId = _cursor.getLong(_cursorIndexOfGraphemeId);
            final int _tmpBox;
            _tmpBox = _cursor.getInt(_cursorIndexOfBox);
            final long _tmpNextReviewAt;
            _tmpNextReviewAt = _cursor.getLong(_cursorIndexOfNextReviewAt);
            _item = new Mastery(_tmpGraphemeId,_tmpBox,_tmpNextReviewAt);
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
  public Object getAll(final Continuation<? super List<Mastery>> $completion) {
    final String _sql = "SELECT * FROM mastery";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Mastery>>() {
      @Override
      @NonNull
      public List<Mastery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGraphemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "graphemeId");
          final int _cursorIndexOfBox = CursorUtil.getColumnIndexOrThrow(_cursor, "box");
          final int _cursorIndexOfNextReviewAt = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewAt");
          final List<Mastery> _result = new ArrayList<Mastery>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Mastery _item;
            final long _tmpGraphemeId;
            _tmpGraphemeId = _cursor.getLong(_cursorIndexOfGraphemeId);
            final int _tmpBox;
            _tmpBox = _cursor.getInt(_cursorIndexOfBox);
            final long _tmpNextReviewAt;
            _tmpNextReviewAt = _cursor.getLong(_cursorIndexOfNextReviewAt);
            _item = new Mastery(_tmpGraphemeId,_tmpBox,_tmpNextReviewAt);
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
  public Object get(final long id, final Continuation<? super Mastery> $completion) {
    final String _sql = "SELECT * FROM mastery WHERE graphemeId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Mastery>() {
      @Override
      @Nullable
      public Mastery call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGraphemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "graphemeId");
          final int _cursorIndexOfBox = CursorUtil.getColumnIndexOrThrow(_cursor, "box");
          final int _cursorIndexOfNextReviewAt = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewAt");
          final Mastery _result;
          if (_cursor.moveToFirst()) {
            final long _tmpGraphemeId;
            _tmpGraphemeId = _cursor.getLong(_cursorIndexOfGraphemeId);
            final int _tmpBox;
            _tmpBox = _cursor.getInt(_cursorIndexOfBox);
            final long _tmpNextReviewAt;
            _tmpNextReviewAt = _cursor.getLong(_cursorIndexOfNextReviewAt);
            _result = new Mastery(_tmpGraphemeId,_tmpBox,_tmpNextReviewAt);
          } else {
            _result = null;
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
