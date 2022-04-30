package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AccountDAO_Impl implements AccountDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Account> __insertionAdapterOfAccount;

  private final EntityDeletionOrUpdateAdapter<Account> __deletionAdapterOfAccount;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBalanceMinus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBalancePlus;

  public AccountDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAccount = new EntityInsertionAdapter<Account>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `account` (`accountNo`,`bankName`,`accountHolderName`,`balance`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Account value) {
        if (value.getAccountNo() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getAccountNo());
        }
        if (value.getBankName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getBankName());
        }
        if (value.getAccountHolderName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getAccountHolderName());
        }
        stmt.bindDouble(4, value.getBalance());
      }
    };
    this.__deletionAdapterOfAccount = new EntityDeletionOrUpdateAdapter<Account>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `account` WHERE `accountNo` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Account value) {
        if (value.getAccountNo() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getAccountNo());
        }
      }
    };
    this.__preparedStmtOfUpdateBalanceMinus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "update account set balance=balance-? where accountNo=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBalancePlus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "update account set balance=balance+? where accountNo=?";
        return _query;
      }
    };
  }

  @Override
  public void addAccount(final Account account) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAccount.insert(account);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeAccount(final Account account) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAccount.handle(account);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBalanceMinus(final String accountNo, final double amount) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBalanceMinus.acquire();
    int _argIndex = 1;
    _stmt.bindDouble(_argIndex, amount);
    _argIndex = 2;
    if (accountNo == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, accountNo);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBalanceMinus.release(_stmt);
    }
  }

  @Override
  public void updateBalancePlus(final String accountNo, final double amount) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBalancePlus.acquire();
    int _argIndex = 1;
    _stmt.bindDouble(_argIndex, amount);
    _argIndex = 2;
    if (accountNo == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, accountNo);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBalancePlus.release(_stmt);
    }
  }

  @Override
  public List<String> getAccountNumbersList() {
    final String _sql = "select accountNo from account";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Account> getAccountsList() {
    final String _sql = "select * from account";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfAccountNo = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNo");
      final int _cursorIndexOfBankName = CursorUtil.getColumnIndexOrThrow(_cursor, "bankName");
      final int _cursorIndexOfAccountHolderName = CursorUtil.getColumnIndexOrThrow(_cursor, "accountHolderName");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final List<Account> _result = new ArrayList<Account>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Account _item;
        final String _tmpAccountNo;
        if (_cursor.isNull(_cursorIndexOfAccountNo)) {
          _tmpAccountNo = null;
        } else {
          _tmpAccountNo = _cursor.getString(_cursorIndexOfAccountNo);
        }
        final String _tmpBankName;
        if (_cursor.isNull(_cursorIndexOfBankName)) {
          _tmpBankName = null;
        } else {
          _tmpBankName = _cursor.getString(_cursorIndexOfBankName);
        }
        final String _tmpAccountHolderName;
        if (_cursor.isNull(_cursorIndexOfAccountHolderName)) {
          _tmpAccountHolderName = null;
        } else {
          _tmpAccountHolderName = _cursor.getString(_cursorIndexOfAccountHolderName);
        }
        final double _tmpBalance;
        _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
        _item = new Account(_tmpAccountNo,_tmpBankName,_tmpAccountHolderName,_tmpBalance);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Account getAccount(final String accountNo) {
    final String _sql = "select * from account where accountNo like ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (accountNo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, accountNo);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfAccountNo = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNo");
      final int _cursorIndexOfBankName = CursorUtil.getColumnIndexOrThrow(_cursor, "bankName");
      final int _cursorIndexOfAccountHolderName = CursorUtil.getColumnIndexOrThrow(_cursor, "accountHolderName");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final Account _result;
      if(_cursor.moveToFirst()) {
        final String _tmpAccountNo;
        if (_cursor.isNull(_cursorIndexOfAccountNo)) {
          _tmpAccountNo = null;
        } else {
          _tmpAccountNo = _cursor.getString(_cursorIndexOfAccountNo);
        }
        final String _tmpBankName;
        if (_cursor.isNull(_cursorIndexOfBankName)) {
          _tmpBankName = null;
        } else {
          _tmpBankName = _cursor.getString(_cursorIndexOfBankName);
        }
        final String _tmpAccountHolderName;
        if (_cursor.isNull(_cursorIndexOfAccountHolderName)) {
          _tmpAccountHolderName = null;
        } else {
          _tmpAccountHolderName = _cursor.getString(_cursorIndexOfAccountHolderName);
        }
        final double _tmpBalance;
        _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
        _result = new Account(_tmpAccountNo,_tmpBankName,_tmpAccountHolderName,_tmpBalance);
      } else {
        _result = null;
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
}
