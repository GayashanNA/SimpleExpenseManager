package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, "190292D.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createAccountTable = "create table account ("+
                "accountNo text primary key, " +
                "bankName text, "+
                "accountHolderName text, "+
                "balance real "+
                ")";

        String createTransactionTable = "create table transactionLog ("+
                "transactionId integer primary key autoincrement, "+
                "date text, " +
                "accountNo text, " +
                "expenseType text check(expenseType in ('EXPENSE','INCOME')), "+
                "amount real, "+
                "foreign key(accountNo) references account(accountNo)"+
                ")";

        sqLiteDatabase.execSQL(createAccountTable);
        sqLiteDatabase.execSQL(createTransactionTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists account");
        sqLiteDatabase.execSQL("drop table if exists transactionLog");
        onCreate(sqLiteDatabase);
    }


    public List<Account> getAccounts(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursorAccounts = sqLiteDatabase.rawQuery("select * from account", null);
        List<Account> accounts = new ArrayList<Account>();
        if(cursorAccounts.moveToFirst()){
            do{
                accounts.add(new Account(cursorAccounts.getString(0), cursorAccounts.getString(1), cursorAccounts.getString(2), cursorAccounts.getDouble(3)));
            }while(cursorAccounts.moveToNext());
        }
        cursorAccounts.close();
        return accounts;
    }

    public List<Transaction> getTransactions(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursorTransactions = sqLiteDatabase.rawQuery("select * from transactionLog", null);
        List<Transaction> transactions = new ArrayList<Transaction>();
        if(cursorTransactions.moveToFirst()){
            do{
                transactions.add(new Transaction(new Date(cursorTransactions.getString(1)), cursorTransactions.getString(2), ExpenseType.valueOf(cursorTransactions.getString(3)), cursorTransactions.getDouble(4)));
            }while(cursorTransactions.moveToNext());
        }
        cursorTransactions.close();
        return transactions;
    }


    public void addAccount(Account account){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("bankName", account.getBankName());
        contentValues.put("accountHolderName", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        sqLiteDatabase.insert("account", null, contentValues);
        sqLiteDatabase.close();
    }

    public void addTransaction(Transaction transaction){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", transaction.getDate().toString());
        contentValues.put("accountNo", transaction.getAccountNo());
        contentValues.put("expenseType", transaction.getExpenseType().name());
        contentValues.put("amount", transaction.getAmount());
        sqLiteDatabase.insert("transaction", null, contentValues);
        sqLiteDatabase.close();
    }

    public void removeAccount(String accountNo){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("account","accountNo=?",new String[]{accountNo});
        sqLiteDatabase.close();
    }

    public void updateAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("bankName", account.getBankName());
        contentValues.put("accountHolderName", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        sqLiteDatabase.update("account", contentValues, "accountNo=?", new String[]{account.getAccountNo()});
    }


}
