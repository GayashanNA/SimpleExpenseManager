package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQLiteAccountDAO implements AccountDAO {

    private SQLiteHelper sqLiteHelper;

    public SQLiteAccountDAO(Context context) {
        sqLiteHelper = SQLiteHelper.getInstance(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNoList = new LinkedList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + SQLiteHelper.ACCOUNT_NO + " FROM "+ SQLiteHelper.ACCOUNT_TABLE, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String account_num = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ACCOUNT_NO));
            accountNoList.add(account_num);
        }
        cursor.close();
        db.close();
        return accountNoList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList = new LinkedList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteHelper.ACCOUNT_TABLE, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String accountNo = cursor.getString(0);
            String bankName = cursor.getString(1);
            String accHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);

            Account account = new Account(accountNo,bankName,accHolderName,balance);
            accountsList.add(account);
        }
        cursor.close();
        db.close();
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String[] arguments = {accountNo};
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteHelper.ACCOUNT_TABLE + " WHERE " +SQLiteHelper.ACCOUNT_NO + " =? ", arguments);

        if(cursor.moveToFirst()){
            String accNo = cursor.getString(0);
            String bankName = cursor.getString(1);
            String accHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);

            return new Account(accNo,bankName,accHolderName,balance);
        }else{
            throw new InvalidAccountException("Invalid account");
        }

    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.ACCOUNT_NO, account.getAccountNo());
        cv.put(SQLiteHelper.BANK_NAME, account.getBankName());
        cv.put(SQLiteHelper.ACC_HOLDER_NAME,account.getAccountHolderName());
        cv.put(SQLiteHelper.BALANCE,account.getBalance());

        long insert = db.insert(SQLiteHelper.ACCOUNT_TABLE, null, cv);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String[] arguments = {accountNo};
        db.delete(SQLiteHelper.ACCOUNT_TABLE, SQLiteHelper.ACCOUNT_NO+ "=?",arguments);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        switch (expenseType){
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
        }

        cv.put(SQLiteHelper.BALANCE,account.getBalance());
        int update = db.update(SQLiteHelper.ACCOUNT_TABLE, cv,SQLiteHelper.ACCOUNT_NO + "=?", new String[] {accountNo});

    }
}
