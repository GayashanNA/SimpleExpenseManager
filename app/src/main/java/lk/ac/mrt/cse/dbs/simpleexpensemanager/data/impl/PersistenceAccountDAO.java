package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.SqliteHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistenceAccountDAO implements AccountDAO {
    private Context context;


    public PersistenceAccountDAO(Context context) {
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase db = sqliteHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = db.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_ACCOUNTS + " ;", null);
        List<String> accountNumbersList = new ArrayList<>();

        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String account_no = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_NO));
                accountNumbersList.add(account_no);
            }
        }
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_ACCOUNTS + " ;", null);
        List<Account> accounts = new ArrayList<>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String account_no = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_NO));
                String bank_name = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_BANK_NAME));
                String account_holder_name = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_HOLDER_NAME));
                double balance = cursor.getDouble(cursor.getColumnIndex(sqliteHandler.COLUMN_BALANCE));
                accounts.add(new Account(account_no, bank_name, account_holder_name, balance));
            }
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_ACCOUNTS + " where " + sqliteHandler.COLUMN_ACCOUNT_NO + " =? " + " ;", new String[] {accountNo});
        if (cursor.getCount() > 0){
            Account account = null;
            while (cursor.moveToNext()){
                String account_no = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_NO));
                String bank_name = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_BANK_NAME));
                String account_holder_name = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_HOLDER_NAME));
                double balance = cursor.getDouble(cursor.getColumnIndex(sqliteHandler.COLUMN_BALANCE));
                account = new Account(account_no, bank_name, account_holder_name, balance);
                break;
            }
            return account;
        }else {
            throw new InvalidAccountException(accountNo + " is a Invalid account number");
        }
    }

    @Override
    public void addAccount(Account account) {
        try {
            getAccount(account.getAccountNo());
        } catch (InvalidAccountException e) {
            SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
            SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("account_no", account.getAccountNo());
            contentValues.put("bank_name", account.getBankName());
            contentValues.put("account_holder_name", account.getAccountHolderName());
            contentValues.put("balance", account.getBalance());
            long result = sqLiteDatabase.insert(sqliteHandler.TABLE_ACCOUNTS, null, contentValues);
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_ACCOUNTS + " WHERE " + sqliteHandler.COLUMN_ACCOUNT_NO + " = ?" + " ;", new String[] {accountNo});
        if (cursor.getCount() > 0){
            sqLiteDatabase.delete(sqliteHandler.TABLE_ACCOUNTS, "account_no=?", new String[]{accountNo});
        }else{
            throw new InvalidAccountException(accountNo + " is a invalid account number.");
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_ACCOUNTS + " WHERE " + sqliteHandler.COLUMN_ACCOUNT_NO + " =? " + " ;", new String[]{accountNo});
        if (cursor.getCount() > 0){
            double pre_balance = 0;
            while (cursor.moveToNext()){
                pre_balance = cursor.getDouble(cursor.getColumnIndex(sqliteHandler.COLUMN_BALANCE));
                break;
            }
            double new_balance = -1;
            switch (expenseType){
                case EXPENSE:
                    new_balance = pre_balance - amount;
                case INCOME:
                    new_balance = pre_balance + amount;
            }
            contentValues.put("balance", new_balance);
            sqLiteDatabase.update(sqliteHandler.TABLE_ACCOUNTS, contentValues, "account_no=?", new String[]{accountNo});
        }else {
            throw new InvalidAccountException(accountNo + " is a invalid account number");
        }

    }
}
