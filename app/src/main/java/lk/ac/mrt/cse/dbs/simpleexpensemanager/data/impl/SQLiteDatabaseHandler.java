package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseConnectionException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper implements DatabaseHandler {
    private static final String DB_NAME = "180176R";
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_TRANSACTION_LOG = "transaction_log";

    private static final String COLUMN_ACCOUNT_NO = "account_no";
    private static final String COLUMN_BANK_NAME = "bank_name";
    private static final String COLUMN_ACCOUNT_HOLDER_NAME = "account_holder_name";
    private static final String COLUMN_BALANCE = "balance";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_EXPENSE_TYPE = "expense_type";
    private static final String COLUMN_AMOUNT = "amount";

    private static final String DDL_ACCOUNT  =  "CREATE TABLE " + TABLE_ACCOUNT + "(" +
            COLUMN_ACCOUNT_NO + " TEXT(100) PRIMARY KEY, " +
            COLUMN_BANK_NAME + " TEXT(100) NOT NULL, " +
            COLUMN_ACCOUNT_HOLDER_NAME + " TEXT(100) NOT NULL, " +
            COLUMN_BALANCE + " REAL NOT NULL)";
    private static final String DDL_TRANSACTION  =  "CREATE TABLE " + TABLE_TRANSACTION_LOG + "(" +
            COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_DATE + " DATE NOT NULL, " +
            COLUMN_ACCOUNT_NO + " TEXT(100) NOT NULL, " +
            COLUMN_EXPENSE_TYPE + " TEXT NOT NULL CHECK (" + COLUMN_EXPENSE_TYPE + "== \"EXPENSE\" OR " + COLUMN_EXPENSE_TYPE + " == \"INCOME\"), " +
            COLUMN_AMOUNT + " REAL NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_ACCOUNT_NO + ") REFERENCES " + TABLE_ACCOUNT + "(" + COLUMN_ACCOUNT_NO + "))";

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final SQLiteDatabase sqlDB;
    private static SQLiteDatabaseHandler instance = null;

    private SQLiteDatabaseHandler(Context context) {
        super(context, "180176R", null, 2);
        sqlDB = this.getWritableDatabase();
    }

    public static SQLiteDatabaseHandler getInstance(Context context) {
        if (instance == null)
            synchronized (SQLiteDatabase.class) {
                if (instance == null)
                    instance = new SQLiteDatabaseHandler(context);
            }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DDL_ACCOUNT);
        sqLiteDatabase.execSQL(DDL_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION_LOG);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public Map<String, Account> fetchAllAccounts() {
        Map<String, Account> accounts = new HashMap<String, Account>();
        Cursor result = this.sqlDB.rawQuery("SELECT * FROM " + TABLE_ACCOUNT, null);

        if (result.moveToFirst()) {
            do {
                accounts.put(
                        result.getString(result.getColumnIndex(COLUMN_ACCOUNT_NO)),
                        new Account(
                                result.getString(result.getColumnIndex(COLUMN_ACCOUNT_NO)),
                                result.getString(result.getColumnIndex(COLUMN_BANK_NAME)),
                                result.getString(result.getColumnIndex((COLUMN_ACCOUNT_HOLDER_NAME))),
                                result.getDouble(result.getColumnIndex(COLUMN_BALANCE))));
            } while(result.moveToNext());
        }
        if (result != null && !result.isClosed()) {
            result.close();
        }
        return accounts;
    }

    @Override
    public void addAccount(Account account) throws DatabaseConnectionException {


        ContentValues cv = new ContentValues(4);
        cv.put(COLUMN_ACCOUNT_NO, account.getAccountNo());
        cv.put(COLUMN_BANK_NAME, account.getBankName());
        cv.put(COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        cv.put(COLUMN_BALANCE, account.getBalance());

        long result = this.sqlDB.insert(TABLE_ACCOUNT,null,cv);
        if (result == -1)
            throw new DatabaseConnectionException("Account insertion to the database failed");
    }

    @Override
    public void removeAccount(String accountNo) throws DatabaseConnectionException {
        String[] whereArgs = {accountNo};
        long result = this.sqlDB.delete(TABLE_ACCOUNT,COLUMN_ACCOUNT_NO + " = ?", whereArgs);

        if (result == 0)
            throw new DatabaseConnectionException("Account deletion in the database failed");
    }

    @Override
    public void updateAccount(Account account) throws DatabaseConnectionException {
        String[] whereArgs = {account.getAccountNo()};

        ContentValues cv = new ContentValues(4);
        cv.put(COLUMN_ACCOUNT_NO,account.getAccountNo());
        cv.put(COLUMN_BANK_NAME,account.getBankName());
        cv.put(COLUMN_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        cv.put(COLUMN_BALANCE,account.getBalance());

        long result = this.sqlDB.update("account",cv,"account_no = ?",whereArgs);
        if (result == 0)
            throw new DatabaseConnectionException("Updating the account in the database failed");
    }

    @Override
    public List<Transaction> fetchAllTransactions() throws ParseException {
        List<Transaction> transactions = new LinkedList<Transaction>();
        Cursor result = this.sqlDB.rawQuery("SELECT * FROM " + TABLE_TRANSACTION_LOG, null);

        if (result.moveToFirst()) {
            do {
                transactions.add(new Transaction(
                        DATE_FORMAT.parse(result.getString(result.getColumnIndex(COLUMN_DATE))),
                        result.getString(result.getColumnIndex(COLUMN_ACCOUNT_NO)),
                        ExpenseType.valueOf(result.getString(result.getColumnIndex(COLUMN_EXPENSE_TYPE))),
                        result.getDouble(result.getColumnIndex(COLUMN_AMOUNT))));
            } while(result.moveToNext());
        }
        if (result != null && !result.isClosed()) {
            result.close();
        }
        return transactions;
    }

    @Override
    public void addTransaction(Transaction transaction) throws DatabaseConnectionException {
        ContentValues cv = new ContentValues(4);
        cv.put(COLUMN_DATE,DATE_FORMAT.format(transaction.getDate()));
        cv.put(COLUMN_ACCOUNT_NO, transaction.getAccountNo());
        cv.put(COLUMN_EXPENSE_TYPE, transaction.getExpenseType().toString());
        cv.put(COLUMN_AMOUNT, transaction.getAmount());

        long result = this.sqlDB.insert(TABLE_TRANSACTION_LOG,null, cv);
        if (result == -1)
            throw new DatabaseConnectionException("Transaction insertion to the database failed");
    }
}
