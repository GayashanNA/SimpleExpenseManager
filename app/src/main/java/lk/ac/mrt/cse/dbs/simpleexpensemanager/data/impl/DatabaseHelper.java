package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType.EXPENSE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType.INCOME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String ACCOUNT_TABLE = "account";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_BANK_NAME = "bank";
    public static final String COLUMN_HOLDER_NAME = "holder";
    public static final String COLUMN_BALANCE = "balance";

    public static final String TRANSACTION_TABLE = "transactionLog";
    public static final String COLUMN_TRANSACTION_ID = "transactionID";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_AMOUNT = "amount";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "simpleExpenseManager.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("mydebug", "IN DBH CLASS!");
        String createAccountTableStatement = "CREATE TABLE " + ACCOUNT_TABLE + "(" +
                COLUMN_ACCOUNT_NO + " TEXT PRIMARY KEY, " + COLUMN_BANK_NAME +
                " TEXT NOT NULL, " + COLUMN_HOLDER_NAME + " TEXT NOT NULL, " +
                COLUMN_BALANCE + " REAL DEFAULT 0)";

        String createTransactionTableStatement ="CREATE TABLE " + TRANSACTION_TABLE + "(" + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT NOT NULL, " + COLUMN_ACCOUNT_NO + " TEXT NOT NULL, " + COLUMN_TYPE + " INTEGER NOT NULL, " + COLUMN_AMOUNT + " REAL NOT NULL, " + "FOREIGN KEY (" + COLUMN_ACCOUNT_NO + ") REFERENCES account(" + COLUMN_ACCOUNT_NO + "))";
        sqLiteDatabase.execSQL( createAccountTableStatement );
        sqLiteDatabase.execSQL( createTransactionTableStatement );
        Log.d("mydebug123", "OnCreate called!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		// on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
 
        // create new tables
        onCreate(sqLiteDatabase);
    }

    public boolean addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NO, account.getAccountNo());
        cv.put(COLUMN_BANK_NAME, account.getBankName());
        cv.put(COLUMN_HOLDER_NAME, account.getAccountHolderName());
        cv.put(COLUMN_BALANCE, account.getBalance());

        long insert = db.insert(ACCOUNT_TABLE, null, cv);
        Log.d("mydebug123", "Result of addAcount() " + insert);
        db.close();
        if(insert == -1) return false;
        else return true;
    }

    public List<Account> getAllAcoounts() {

        List<Account> allAccounts = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + ACCOUNT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String accNo = cursor.getString(0);
                String bank = cursor.getString(1);
                String holder = cursor.getString(2);
                double balance = cursor.getDouble(3);

                Log.d("mydebug123", "getting all accounts " + accNo);

                Account acc = new Account(accNo, bank, holder, balance);
                allAccounts.add(acc);

            } while (cursor.moveToNext());
        } else {
            Log.d("mydebug123", "NO results fetched from getAllAcoounts() method");
        }

        cursor.close();
        db.close();
        return allAccounts;
    }

    public Account getAccount(String accNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NO + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{accNo});
        if (cursor.moveToFirst()) {
            String res_accNo = cursor.getString(0);
            String res_bank = cursor.getString(1);
            String res_holder = cursor.getString(2);
            double res_balance = cursor.getDouble(3);

            Log.d("mydebug123", "Getting one account " + accNo);

            Account result_account = new Account(res_accNo, res_bank, res_holder, res_balance);

            cursor.close();
            db.close();
            return result_account;
        } else {
            db.close();
            return null;
        }
    }

    public boolean removeAccount(String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(ACCOUNT_TABLE, COLUMN_ACCOUNT_NO + " = ?", new String[]{accountNo});
        Log.d("mydebug123", "result of removeAccount() " + deletedRows);

        db.close();
        if(deletedRows == 0) return false;
        else return true;
    }

    public boolean updateBalance(String accountNo, ExpenseType expenseType, double amount) {

        ContentValues cv = new ContentValues();
        Account acc = getAccount(accountNo);
        if (acc ==null) {
            Log.d("mydebug123", "Returned acc is NULL!");
            return false;
        }

        double currentBalance = acc.getBalance();
        double newBalance = expenseType == EXPENSE ? currentBalance - amount : currentBalance + amount;
        cv.put(COLUMN_BALANCE, newBalance);
        cv.put(COLUMN_HOLDER_NAME, acc.getAccountHolderName());
        cv.put(COLUMN_BANK_NAME, acc.getBankName());
        cv.put(COLUMN_ACCOUNT_NO, acc.getAccountNo());

        // updating row
        SQLiteDatabase db = this.getWritableDatabase();
        int updatedRows = db.update(ACCOUNT_TABLE, cv, COLUMN_ACCOUNT_NO + " = ?",
                new String[]{accountNo});
        Log.d("mydebug123", "result of updateBalance() " + updatedRows);

        db.close();
        if(updatedRows == 0) return false;
        else return true;
    }

    public boolean logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(date);
        cv.put(COLUMN_DATE, strDate);
        cv.put(COLUMN_ACCOUNT_NO, accountNo);
        
        int type = expenseType == EXPENSE ? 0 : 1;
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_AMOUNT, amount);

        long insert = db.insert(TRANSACTION_TABLE, null, cv);
        Log.d("mydebug123", "Result of logTransaction() " + insert);

        db.close();
        if(insert == -1) return false;
        else return true;
    }

    public List<Transaction> getAllTransactionLogs() {

        List<Transaction> allTransactions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TRANSACTION_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String strDate = cursor.getString(1);
                String accNo = cursor.getString(2);
                ExpenseType expenseType = cursor.getInt(3) == 0 ? EXPENSE : INCOME;
                double amount = cursor.getDouble(4);

                Log.d("mydebug123", "getting all transactions " + cursor.getInt(0));

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date date = formatter.parse(strDate);
                    Transaction transaction = new Transaction(date, accNo, expenseType, amount);
                    allTransactions.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("mydebug123", "Error when converting strDate to Date " + cursor.getInt(0));
                }

            } while (cursor.moveToNext());
        } else {
            Log.d("mydebug123", "NO results fetched from getAllTransactionLogs() method");
        }

        cursor.close();
        db.close();
        return allTransactions;
    }
}
