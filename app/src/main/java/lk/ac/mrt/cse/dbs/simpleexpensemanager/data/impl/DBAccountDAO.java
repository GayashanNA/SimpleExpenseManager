package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBAccountDAO implements AccountDAO {
    private SQLiteDB SQLiteDB;

    public DBAccountDAO(SQLiteDB SQLiteDB) {
        this.SQLiteDB = SQLiteDB;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();

        SQLiteDatabase db = SQLiteDB.getReadableDatabase();
        Cursor cursor = db.query(SQLiteDB.TABLE_ACCOUNTS, new String[] {SQLiteDB.KEY_ACCOUNT_NO}, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding account to list
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = SQLiteDB.getReadableDatabase();
        Cursor cursor = db.query(SQLiteDB.TABLE_ACCOUNTS, null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                // Adding account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = SQLiteDB.getReadableDatabase();
        Cursor cursor = db.query(SQLiteDB.TABLE_ACCOUNTS, null, SQLiteDB.KEY_ACCOUNT_NO + "=?",
                new String[] { accountNo }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = SQLiteDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDB.KEY_ACCOUNT_NO, account.getAccountNo()); // Account No
        values.put(SQLiteDB.KEY_BANK_NAME, account.getBankName()); // Bank Name
        values.put(SQLiteDB.KEY_ACCOUNT_HOLDER_NAME, account.getAccountHolderName()); // Holder Name
        values.put(SQLiteDB.KEY_BALANCE, account.getBalance()); // Balance

        // Inserting Row
        db.insert(SQLiteDB.TABLE_ACCOUNTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = SQLiteDB.getWritableDatabase();
        db.delete(SQLiteDB.TABLE_ACCOUNTS, SQLiteDB.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = SQLiteDB.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(SQLiteDB.KEY_BALANCE, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(SQLiteDB.KEY_BALANCE, account.getBalance() + amount);
                break;
        }
        // updating row
        db.update(SQLiteDB.TABLE_ACCOUNTS, values, SQLiteDB.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
    }
}
