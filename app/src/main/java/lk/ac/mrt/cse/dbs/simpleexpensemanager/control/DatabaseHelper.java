package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;
    private static final String DATABASE_NAME = "androidMini.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE account(accountNo TEXT, bankName TEXT, holderName TEXT, balance DECIMAL(10,2), isActive TINYINT(1), primary key(accountNo))";
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE trans(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, date TEXT, accountNo TEXT, expenseType TEXT, amount DECIMAL(10, 2), FOREIGN KEY(accountNo) REFERENCES account(accountNo) ON UPDATE CASCADE ON DELETE NO ACTION)";

        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS account");
            db.execSQL("DROP TABLE IF EXISTS trans" );
            onCreate(db);
        }
    }

    public void addAccount(Account account) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("accountNo", account.getAccountNo());
            values.put("bankName", account.getBankName());
            values.put("holderName", account.getAccountHolderName());
            values.put("balance", account.getBalance());
            values.put("isActive", 1);

            int rows = db.update("account", values, "accountNo = ?", new String[]{account.getAccountNo()});
            if(rows != 1) {
                db.insertOrThrow("account", null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error Adding new Account");
        } finally {
            db.endTransaction();
        }
    }

    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();

        String ACCOUNT_NUMBER_SELECT_QUERY = "SELECT accountNo FROM account";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ACCOUNT_NUMBER_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    accountNumbers.add(cursor.getString(cursor.getColumnIndex("accountNo")));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get accounts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return accountNumbers;
    }

    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();

        String ACCOUNT_SELECT_QUERY = "SELECT accountNo, bankName, holderName, balance FROM account";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ACCOUNT_SELECT_QUERY, null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    accounts.add(new Account(cursor.getString(cursor.getColumnIndex("accountNo")), cursor.getString(cursor.getColumnIndex("bankName")), cursor.getString(cursor.getColumnIndex("holderName")), cursor.getDouble(cursor.getColumnIndex("balance"))));
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to get accounts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return accounts;
    }

    public Account getAccount(String accountNo) {
        Account account;
        String ACCOUNT_SELECT_QUERY = "SELECT accountNo, bankName, holderName, balance FROM account WHERE accountNo=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ACCOUNT_SELECT_QUERY, new String[]{accountNo});

        try {
            if(cursor.getCount() == 0) {
                return null;
            }
            if(cursor.moveToFirst()) {
                account = new Account(cursor.getString(cursor.getColumnIndex("accountNo")), cursor.getString(cursor.getColumnIndex("bankName")), cursor.getString(cursor.getColumnIndex("holderName")), cursor.getDouble(cursor.getColumnIndex("balance")));
                return account;
            }
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public boolean removeAccount(String accountNo) {
        Account account = this.getAccount(accountNo);
        if(account == null)  {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("accountNo", account.getAccountNo());
            values.put("bankName", account.getBankName());
            values.put("holderName", account.getAccountHolderName());
            values.put("balance", account.getBalance());
            values.put("isActive", 0);

            db.update("account", values, "accountNo = ?", new String[]{account.getAccountNo()});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying remove account");
        } finally {
            db.endTransaction();
            return true;
        }
    }

    public boolean updateBalance(String accountNo, String expenseType, double amount) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        boolean output = false;
        try {
            Account account = this.getAccount(accountNo);

            if(account == null) {
                return output;
            }else {
                double finalAmount = account.getBalance();
                switch (expenseType){
                    case "EXPENSE":
                        finalAmount = finalAmount - amount;
                        break;
                    case "INCOME":
                        finalAmount = finalAmount + amount;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + expenseType);
                }

                ContentValues values = new ContentValues();
                values.put("accountNo", account.getAccountNo());
                values.put("bankName", account.getBankName());
                values.put("holderName", account.getAccountHolderName());
                values.put("balance", finalAmount);
                values.put("isActive", 1);

                db.update("account", values, "accountNo=?", new String[]{accountNo});
                db.setTransactionSuccessful();
                output = true;
            }

        }catch(Exception e) {
            Log.d(TAG, "Error while trying to update balance");
        }finally {
            db.endTransaction();
            return output;
        }
    }

    public void logTransaction(Date date, String accountNo, String expenseType, double amount) {
        SQLiteDatabase db = getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);


        db.execSQL("INSERT INTO trans(date, accountNo, expenseType, amount) VALUES(?,?,?,?)", new String[]{dateString, accountNo, expenseType, amount+""});
    }

    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();

        String TRANSACTION_SELECT_QUERY = "SELECT date, accountNo, expenseType, amount FROM trans";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(TRANSACTION_SELECT_QUERY, null);

        try {
            if(cursor.getCount()>0 && cursor.moveToFirst()) {
                do {
                    transactions.add(new Transaction((new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex("date")))), cursor.getString(cursor.getColumnIndex("accountNo")), ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex("expenseType"))), Double.parseDouble(cursor.getString(cursor.getColumnIndex("amount")))));

                }while(cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "Error while trying to get Transactions Logs");
        }finally {
            return transactions;
        }
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>();

        transactions = this.getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);
    }

    public double getAccountBalance(String accountNo) {
        Account account = this.getAccount(accountNo);
        return account.getBalance();
    }

}
