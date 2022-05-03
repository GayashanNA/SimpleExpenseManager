package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    private static final int DB_VERSION = 1;
    public PersistentAccountDAO(Context context, String dbname) {
        super(context, dbname, null, DB_VERSION );
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT account_number from account", null);
        if(cursor.moveToFirst()){
            do{
                accountNumberList.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM account", null);
        if(cursor.moveToFirst()){
            do{
                accountsList.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3) ));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM account WHERE account_number = ?", new String[]{accountNo});
        if(cursor.moveToFirst()) account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3));
        else account = null;
        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO account(account_number, bank_name, account_holder, balance) VALUES (?, ?, ?, ?)", new Object[]{account.getAccountNo(), account.getBankName(), account.getAccountHolderName(), account.getBalance()});
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM account WHERE account_number = ?", new Object[] {accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        double currentBalance ;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try{
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT balance FROM account WHERE account_number = ?", new String[]{accountNo});
            if( cursor.moveToFirst()){
                currentBalance = (double) cursor.getFloat(0);
            }
            else {
                throw new InvalidAccountException("Account not found");
            }
            cursor.close();
        }catch (Exception e){
            throw new InvalidAccountException("No Account Selected");
        }

        sqLiteDatabase = this.getWritableDatabase();
        double updatedBalance = expenseType==ExpenseType.EXPENSE ? currentBalance-amount: currentBalance+amount;
        try {
            sqLiteDatabase.execSQL("UPDATE account SET balance = ? WHERE account_number = ?", new Object[]{updatedBalance, accountNo  });
        }catch (SQLiteConstraintException ignored){
            throw new InvalidAccountException("Account balance will be less than 0");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS account(account_number VARCHAR(15) NOT NULL PRIMARY KEY, bank_name VARCHAR(100) NOT NULL, account_holder VARCHAR(100) NOT NULL, balance FLOAT(10,2) NOT NULL CHECK (balance >= 0))";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
