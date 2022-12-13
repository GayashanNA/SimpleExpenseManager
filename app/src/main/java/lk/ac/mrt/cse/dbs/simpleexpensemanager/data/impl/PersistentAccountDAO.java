package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.ACCOUNT_HOLDER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.ACCOUNT_TBL;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.BALANCE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.BANK_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO  implements AccountDAO {

    private final DatabaseUtil dbUtil;
    private SQLiteDatabase db;

    public PersistentAccountDAO(Context context) {
        dbUtil = new DatabaseUtil(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        db = dbUtil.getReadableDatabase();

        String[] schema = {
                ACCOUNT_NO
        };
        Cursor cursor = db.query(
                ACCOUNT_TBL,
                schema,
                null,
                null,
                null,
                null,
                null
        );
        List<String> accountNumbers = new ArrayList<>();

        while(cursor.moveToNext()) {
            String accountNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(ACCOUNT_NO));
            accountNumbers.add(accountNumber);
        }
        cursor.close();
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();

        db = dbUtil.getReadableDatabase();

        String[] schema = {
                ACCOUNT_NO,
                BANK_NAME,
                ACCOUNT_HOLDER,
                BALANCE
        };

        Cursor cursor = db.query(
                ACCOUNT_TBL,
                schema,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String accountNumber = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String bankName = cursor.getString(cursor.getColumnIndex(BANK_NAME));
            String accountHolderName = cursor.getString(cursor.getColumnIndex(ACCOUNT_HOLDER));
            double balance = cursor.getDouble(cursor.getColumnIndex(BALANCE));
            Account account = new Account(accountNumber,bankName,accountHolderName,balance);

            accounts.add(account);
        }
        cursor.close();
        return accounts;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        db = dbUtil.getReadableDatabase();
        String[] schema = {
                ACCOUNT_NO,
                BANK_NAME,
                ACCOUNT_HOLDER,
                BALANCE
        };

        String selection = ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        Cursor c = db.query(
                ACCOUNT_TBL,
                schema,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c == null){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        else {
            c.moveToFirst();

            Account account = new Account(accountNo, c.getString(c.getColumnIndex(BANK_NAME)),
                    c.getString(c.getColumnIndex(ACCOUNT_HOLDER)), c.getDouble(c.getColumnIndex(BALANCE)));
            return account;
        }
    }

    @Override
    public void addAccount(Account account) {
        db = dbUtil.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, account.getAccountNo());
        values.put(BANK_NAME, account.getBankName());
        values.put(ACCOUNT_HOLDER, account.getAccountHolderName());
        values.put(BALANCE,account.getBalance());

        db.insert(ACCOUNT_TBL, null, values);
        db.close();
    }

    @Override
    public void removeAccount(String acc) throws InvalidAccountException {
        try {
            db = dbUtil.getWritableDatabase();
            db.delete(ACCOUNT_TBL, ACCOUNT_NO + " = ?",
                    new String[] { acc });
            db.close();
        }
        catch (Exception e) {
            throw new InvalidAccountException(acc);
        }
    }

    @Override
    public void updateBalance(String acc, ExpenseType expenseType, double amount) throws InvalidAccountException {
        db = dbUtil.getWritableDatabase();
        String[] schema = {
                BALANCE
        };

        String select = ACCOUNT_NO + " = ?";
        String[] args = { acc };

        Cursor cursor = db.query(
                ACCOUNT_TBL,
                schema,
                select,
                args,
                null,
                null,
                null
        );

        double balance;
        if(cursor.moveToFirst())
            balance = cursor.getDouble(0);
        else{
            String msg = "Account " + acc + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(BALANCE, balance - amount);
                break;
            case INCOME:
                values.put(BALANCE, balance + amount);
                break;
        }

        db.update(ACCOUNT_TBL, values, ACCOUNT_NO + " = ?",
                new String[] { acc });

        cursor.close();
        db.close();
    }
}
