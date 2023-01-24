package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import android.annotation.SuppressLint;
import android.database.Cursor;
import  android.database.sqlite.*;

public class PersistentAccountDAO implements AccountDAO {
    SQLiteDatabase db;
    java.io.File filename = Constants.CONTEXT.getFilesDir();
    public PersistentAccountDAO()
    {
        db = SQLiteDatabase.openOrCreateDatabase(filename.getAbsolutePath() + "/200592R.sqlite", null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Account(accountNo VARCHAR(50),bankName VARCHAR(50)," +
                "accountHolderName VARCHAR(50), balance NUMERIC(10,2));");
    }

    @Override
    public List<String> getAccountNumbersList() {
        @SuppressLint("Recycle") Cursor results = db.rawQuery("Select accountNo from Account",null);
        List<String> result = new ArrayList<>();
        results.moveToFirst();
        while(!results.isAfterLast())
        {
            result.add(results.getString(0));
            results.moveToNext();
        }
        return result;
    }

    @Override
    public List<Account> getAccountsList() {
        @SuppressLint("Recycle") Cursor results = db.rawQuery("Select * from Account;",null);
        List<Account> result = new ArrayList<>();
        results.moveToFirst();

        while(!results.isAfterLast())
        {

            result.add( new Account(results.getString(0),results.getString(1),results.getString(2),
                    Double.parseDouble(results.getString(3) ) ));

            results.moveToNext();
        }
        return result;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        @SuppressLint("Recycle") Cursor resultSet = db.rawQuery("Select * from Account where accountNo='"
                + accountNo+"';", null);

        resultSet.moveToFirst();

        if (resultSet.isAfterLast()) {
            throw new InvalidAccountException("Account No:" + accountNo + " is not valid!");
        }
        return new Account(resultSet.getString(0), resultSet.getString(1), resultSet.getString(2),
                Double.parseDouble(resultSet.getString(3)));
    }

    @Override
    public void addAccount(Account account) {
        db.execSQL("INSERT INTO Account VALUES('"+account.getAccountNo()+"','"+account.getBankName()+
                "','"+account.getAccountHolderName()+"','"+account.getBalance()+"');");
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.execSQL("DELETE FROM Account WHERE accountNo='"+accountNo+"';");
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);

        double balance = account.getBalance();
        if (ExpenseType.INCOME == expenseType) {
            balance += amount;
        } else
            balance-=amount;
        db.execSQL("UPDATE Account SET balance='"+balance+"' WHERE accountNo='"+accountNo+"';");
    }
}