
package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseAccountDAO implements AccountDAO {
    private SQLiteDatabase db;

    public DatabaseAccountDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT account_no FROM account;",null);

        if (!cursor.moveToFirst()) {
            return accountNumbers;
        }

        String account_no;

        do {

            account_no = cursor.getString(0);
            accountNumbers.add(account_no);

        } while (cursor.moveToNext());

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<Account>();
        Cursor cursor = db.rawQuery("SELECT * FROM account;",null);

        if (!cursor.moveToFirst()) {
            return accounts;
        }

        Account account;

        do {

            account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            accounts.add(account);

        } while (cursor.moveToNext());

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE account_no='"+accountNo+"';",null);
        if (cursor.moveToFirst()) {
            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {

        db.execSQL("INSERT INTO account VALUES ('"+account.getAccountNo()+"', '"+account.getBankName()+"', '"+account.getAccountHolderName()+"', "+account.getBalance()+")");
        return;
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = db.rawQuery("SELECT * FROM account WHERE account_no='"+accountNo+"';",null);
        if (resultSet.moveToFirst()) {
            db.execSQL("DELETE FROM account WHERE account_no='"+accountNo+"';");
            return;

        }

        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Cursor resultSet = db.rawQuery("SELECT * FROM account WHERE account_no='"+accountNo+"';",null);
        if (!resultSet.moveToFirst()) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        double balance = resultSet.getDouble(3);
        double newBalance;

        switch (expenseType) {
            case EXPENSE:
                newBalance = balance - amount;
                db.execSQL("UPDATE TABLE account SET balance="+newBalance+" WHERE account_no='"+accountNo+"';");
                break;
            case INCOME:
                newBalance = balance + amount;
                db.execSQL("UPDATE TABLE account SET balance="+newBalance+" WHERE account_no='"+accountNo+"';");
                break;
        }
    }
}
