package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TransactionDAO_Impl implements TransactionDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Transaction> __insertionAdapterOfTransaction;

  public TransactionDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransaction = new EntityInsertionAdapter<Transaction>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `transaction` (`year`,`month`,`date`,`trans_no`,`accountNo`,`expenseType`,`amount`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Transaction value) {
        stmt.bindLong(1, value.year);
        stmt.bindLong(2, value.month);
        stmt.bindLong(3, value.date);
        if (value.trans_no == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.trans_no);
        }
        if (value.getAccountNo() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getAccountNo());
        }
        if (value.getExpenseType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, __ExpenseType_enumToString(value.getExpenseType()));
        }
        stmt.bindDouble(7, value.getAmount());
      }
    };
  }

  @Override
  public void logTransaction(final Transaction transaction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTransaction.insert(transaction);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Transaction> getAllTransactionLogs() {
    final String _sql = "select * from `transaction`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
      final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTransNo = CursorUtil.getColumnIndexOrThrow(_cursor, "trans_no");
      final int _cursorIndexOfAccountNo = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNo");
      final int _cursorIndexOfExpenseType = CursorUtil.getColumnIndexOrThrow(_cursor, "expenseType");
      final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
      final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Transaction _item;
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        final int _tmpDate;
        _tmpDate = _cursor.getInt(_cursorIndexOfDate);
        final String _tmpTrans_no;
        if (_cursor.isNull(_cursorIndexOfTransNo)) {
          _tmpTrans_no = null;
        } else {
          _tmpTrans_no = _cursor.getString(_cursorIndexOfTransNo);
        }
        final String _tmpAccountNo;
        if (_cursor.isNull(_cursorIndexOfAccountNo)) {
          _tmpAccountNo = null;
        } else {
          _tmpAccountNo = _cursor.getString(_cursorIndexOfAccountNo);
        }
        final ExpenseType _tmpExpenseType;
        _tmpExpenseType = __ExpenseType_stringToEnum(_cursor.getString(_cursorIndexOfExpenseType));
        final double _tmpAmount;
        _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
        _item = new Transaction(_tmpTrans_no,_tmpYear,_tmpMonth,_tmpDate,_tmpAccountNo,_tmpExpenseType,_tmpAmount);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Transaction> getPaginatedTransactionLogs(final int limit) {
    final String _sql = "select * from `transaction` limit ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
      final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTransNo = CursorUtil.getColumnIndexOrThrow(_cursor, "trans_no");
      final int _cursorIndexOfAccountNo = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNo");
      final int _cursorIndexOfExpenseType = CursorUtil.getColumnIndexOrThrow(_cursor, "expenseType");
      final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
      final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Transaction _item;
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        final int _tmpDate;
        _tmpDate = _cursor.getInt(_cursorIndexOfDate);
        final String _tmpTrans_no;
        if (_cursor.isNull(_cursorIndexOfTransNo)) {
          _tmpTrans_no = null;
        } else {
          _tmpTrans_no = _cursor.getString(_cursorIndexOfTransNo);
        }
        final String _tmpAccountNo;
        if (_cursor.isNull(_cursorIndexOfAccountNo)) {
          _tmpAccountNo = null;
        } else {
          _tmpAccountNo = _cursor.getString(_cursorIndexOfAccountNo);
        }
        final ExpenseType _tmpExpenseType;
        _tmpExpenseType = __ExpenseType_stringToEnum(_cursor.getString(_cursorIndexOfExpenseType));
        final double _tmpAmount;
        _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
        _item = new Transaction(_tmpTrans_no,_tmpYear,_tmpMonth,_tmpDate,_tmpAccountNo,_tmpExpenseType,_tmpAmount);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __ExpenseType_enumToString(final ExpenseType _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case EXPENSE: return "EXPENSE";
      case INCOME: return "INCOME";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private ExpenseType __ExpenseType_stringToEnum(final String _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case "EXPENSE": return ExpenseType.EXPENSE;
      case "INCOME": return ExpenseType.INCOME;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
