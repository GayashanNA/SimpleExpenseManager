package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.InMemoryDemoExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    public DBAccountDAO(@Nullable Context context) {
        super(context, "190081F.db", null, 1);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>() ;
        String queryStmt = "SELECT " + Constants.ACCOUNT_NO_COL + " FROM " + Constants.ACCOUNT_TABLE + " ;" ;

        SQLiteDatabase db = this.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(queryStmt, null);

        if (cursor.moveToFirst()) {
            do {
                accountNumbers.add(cursor.getString(0)) ;
            }while (cursor.moveToNext()) ;

        }else {
            //void
        }
        return accountNumbers ;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>() ;
        String queryStmt = "SELECT * FROM " + Constants.ACCOUNT_TABLE + " ;" ;

        SQLiteDatabase db = getReadableDatabase() ;
        Cursor cursor = db.rawQuery(queryStmt, null) ;

        if (cursor.moveToFirst()){
            do {
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1) ;
                String holderName = cursor.getString(2);
                Double balance = cursor.getDouble(3);
                Account rowAccount = new Account(accountNo,bankName,holderName,balance) ;
                accountList.add(rowAccount) ;
            }while (cursor.moveToNext()) ;
        }else{
            //void
        }
        return accountList ;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account rowAccount = null;
        String queryStmt = "SELECT * FROM " + Constants.ACCOUNT_TABLE + " WHERE " + Constants.ACCOUNT_NO_COL + " = '" + accountNo + "' ;" ;

        SQLiteDatabase db = this.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(queryStmt, null) ;

        if (cursor.moveToFirst()) {
            do {
                String bankName = cursor.getString(1);
                String holderName = cursor.getString(2) ;
                Double balance = cursor.getDouble(3);
                rowAccount = new Account(accountNo, bankName, holderName, balance) ;
            }while (cursor.moveToNext()) ;
        }else {
            //void
        }

        return rowAccount ;
    }

    @Override
    public void addAccount(Account account) {
        String addAccountStmt = "" ;

        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;

        cv.put(Constants.ACCOUNT_NO_COL, account.getAccountNo());
        cv.put(Constants.BANK_NAME_COL, account.getBankName()) ;
        cv.put(Constants.HOLDER_NAME_COL, account.getAccountHolderName()) ;
        cv.put(Constants.BALANCE_COL, account.getBalance()) ;

        db.insert(Constants.ACCOUNT_TABLE,null,cv ) ;

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase() ;
        String queryStmt = "DELETE FROM " + Constants.ACCOUNT_TABLE + " WHERE " + Constants.ACCOUNT_NO_COL + " = '" + accountNo + "' ;" ;

        db.execSQL(queryStmt);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db  = this.getWritableDatabase() ;
        String getBalanceQuery = "SELECT " + Constants.BALANCE_COL + " FROM " + Constants.ACCOUNT_TABLE + " WHERE " + Constants.ACCOUNT_NO_COL + " = " + accountNo + " ;" ;
        Cursor cursor = db.rawQuery(getBalanceQuery,null) ;
        if (cursor.moveToFirst()){
            Double currentBalance = cursor.getDouble(0) ;
            Double nextBalance ;
            if (expenseType == ExpenseType.EXPENSE) {
                nextBalance = currentBalance - amount ;
            }else {
                nextBalance = currentBalance + amount ;
            }


        }else {
            //void
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccountTableStmt = "" ;

        createAccountTableStmt = "CREATE TABLE " + Constants.ACCOUNT_TABLE + " (" + Constants.ACCOUNT_NO_COL + " TEXT PRIMARY KEY, " + Constants.BANK_NAME_COL + " TEXT, " + Constants.HOLDER_NAME_COL + " TEXt, " + Constants.BALANCE_COL + " REAl) ;";

        db.execSQL(createAccountTableStmt);
        String createTransactionTableStmt = "" ;
        createTransactionTableStmt = "CREATE TABLE " + Constants.TRANS_RECORD_TABLE + " (" + Constants.RECORD_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ Constants.ACCOUNT_NO_COL + " TEXT, " + Constants.EXPENSE_TYPE_COL + " TEXT, " + Constants.AMOUNT_COL + " REAl, " + Constants.RECORD_DATE_COL + " TEXT, FOREIGN KEY("+Constants.ACCOUNT_NO_COL +") REFERENCES "+ Constants.ACCOUNT_TABLE +"("+ Constants.ACCOUNT_NO_COL +")) ;" ;
        db.execSQL(createTransactionTableStmt);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
