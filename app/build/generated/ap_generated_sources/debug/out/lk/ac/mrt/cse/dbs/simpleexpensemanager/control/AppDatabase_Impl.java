package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO_Impl;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO_Impl;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AccountDAO _accountDAO;

  private volatile TransactionDAO _transactionDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `account` (`accountNo` TEXT NOT NULL, `bankName` TEXT, `accountHolderName` TEXT, `balance` REAL NOT NULL, PRIMARY KEY(`accountNo`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `transaction` (`year` INTEGER NOT NULL, `month` INTEGER NOT NULL, `date` INTEGER NOT NULL, `trans_no` TEXT NOT NULL, `accountNo` TEXT, `expenseType` TEXT, `amount` REAL NOT NULL, PRIMARY KEY(`trans_no`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b80cc52b308443a2f345bd2b7dca052d')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `account`");
        _db.execSQL("DROP TABLE IF EXISTS `transaction`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsAccount = new HashMap<String, TableInfo.Column>(4);
        _columnsAccount.put("accountNo", new TableInfo.Column("accountNo", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccount.put("bankName", new TableInfo.Column("bankName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccount.put("accountHolderName", new TableInfo.Column("accountHolderName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccount.put("balance", new TableInfo.Column("balance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccount = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccount = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccount = new TableInfo("account", _columnsAccount, _foreignKeysAccount, _indicesAccount);
        final TableInfo _existingAccount = TableInfo.read(_db, "account");
        if (! _infoAccount.equals(_existingAccount)) {
          return new RoomOpenHelper.ValidationResult(false, "account(lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account).\n"
                  + " Expected:\n" + _infoAccount + "\n"
                  + " Found:\n" + _existingAccount);
        }
        final HashMap<String, TableInfo.Column> _columnsTransaction = new HashMap<String, TableInfo.Column>(7);
        _columnsTransaction.put("year", new TableInfo.Column("year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaction.put("month", new TableInfo.Column("month", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaction.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaction.put("trans_no", new TableInfo.Column("trans_no", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaction.put("accountNo", new TableInfo.Column("accountNo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaction.put("expenseType", new TableInfo.Column("expenseType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaction.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransaction = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransaction = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTransaction = new TableInfo("transaction", _columnsTransaction, _foreignKeysTransaction, _indicesTransaction);
        final TableInfo _existingTransaction = TableInfo.read(_db, "transaction");
        if (! _infoTransaction.equals(_existingTransaction)) {
          return new RoomOpenHelper.ValidationResult(false, "transaction(lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction).\n"
                  + " Expected:\n" + _infoTransaction + "\n"
                  + " Found:\n" + _existingTransaction);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "b80cc52b308443a2f345bd2b7dca052d", "48af6a25e6805094313cbb0886d25fec");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "account","transaction");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `account`");
      _db.execSQL("DELETE FROM `transaction`");
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
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AccountDAO.class, AccountDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransactionDAO.class, TransactionDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public AccountDAO getAccountDAO() {
    if (_accountDAO != null) {
      return _accountDAO;
    } else {
      synchronized(this) {
        if(_accountDAO == null) {
          _accountDAO = new AccountDAO_Impl(this);
        }
        return _accountDAO;
      }
    }
  }

  @Override
  public TransactionDAO getTransactionDAO() {
    if (_transactionDAO != null) {
      return _transactionDAO;
    } else {
      synchronized(this) {
        if(_transactionDAO == null) {
          _transactionDAO = new TransactionDAO_Impl(this);
        }
        return _transactionDAO;
      }
    }
  }
}
