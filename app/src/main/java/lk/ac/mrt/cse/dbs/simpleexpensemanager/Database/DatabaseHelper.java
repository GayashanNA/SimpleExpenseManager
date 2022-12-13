package lk.ac.mrt.cse.dbs.simpleexpensemanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper dbHelper = null;
    private static final int Version = 1;
    public static final String DBname = "Accounts_Database.db";

    public static final String Table_Name0 = "Accounts" ;
    public static final String Table_Name1 = "Transactions" ;
    private static final String ACCOUNT_NUMBER = "Account_Number";
    private static final String BALANCE = "Balance";
    private static final String BANK_NAME ="Bank_Name" ;
    private static final String ACCOUNT_HOLDER_NAME = "Account_Holder_Name";
    private static final String DATE = "Date";
    private static final String EXPENSE_TYPE = "Transaction_Type";
    private static final String AMOUNT = "Amount";
    private static final String ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DBname, null , Version);
    }
    public static DatabaseHelper getInstance(Context context){
        if (dbHelper == null){
            dbHelper = new DatabaseHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String TABLE_CREATE_QUERY_accounts =  "CREATE TABLE "+Table_Name0+" "+
                "("
                +ACCOUNT_NUMBER+" TEXT PRIMARY KEY, "
                +BANK_NAME+ " TEXT,"
                +ACCOUNT_HOLDER_NAME+" TEXT,"
                +BALANCE+" REAL check (" +BALANCE+">0)"+
                ");";
        /* CREATE TABLE Accounts (Account Number INTEGER PRIMARY KEY, Bank_Name TEXT, Account_Holder_Name TEXT,Balance REAL check (Balance>0)); */

        String TABLE_CREATE_QUERY_transactions = "CREATE TABLE "+Table_Name1+" "+
                "("
                +ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DATE+" TEXT, "
                +ACCOUNT_NUMBER+" TEXT, "
                +EXPENSE_TYPE+" TEXT, "
                +AMOUNT+" REAL check("+AMOUNT+">0)"
                +");";
         /* CREATE TABLE Transactions (ID INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, Account_Number TEXT, Transaction_Type TEXT, Amount REAL check (Amount>0)); */

        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY_accounts);
        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY_transactions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_TABLE_QUERY_accounts = "DROP TABLE IF EXISTS "+Table_Name0;
        String DROP_TABLE_QUERY_transactions = "DROP TABLE IF EXISTS "+Table_Name1;

        sqLiteDatabase.execSQL(DROP_TABLE_QUERY_accounts);
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY_transactions);

        onCreate(sqLiteDatabase);
    }
    public void addAccount(Account account){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ACCOUNT_NUMBER,account.getAccountNo());
        contentValues.put(BANK_NAME,account.getBankName());
        contentValues.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(BALANCE,account.getBalance());

        sqLiteDatabase.insert(Table_Name0,null,contentValues);

        sqLiteDatabase.close();
    }

    public List<Account> getAccounts(){
        List<Account> AccountsList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT* FROM "+Table_Name0;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                Double balance = cursor.getDouble(3);

                Account account = new Account(accountNo,bankName,accountHolderName,balance);
                AccountsList.add(account);

            }while(cursor.moveToNext());
        }
        return AccountsList;
    }

    public void addTransactions(Transaction transaction){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE,new SimpleDateFormat("dd-MM-yyyy").format(transaction.getDate()));
        contentValues.put(ACCOUNT_NUMBER,transaction.getAccountNo());
        contentValues.put(EXPENSE_TYPE,transaction.getExpenseType().toString());
        contentValues.put(AMOUNT,transaction.getAmount());

        sqLiteDatabase.insert(Table_Name1,null,contentValues);
        sqLiteDatabase.close();
    }

    public Map<Integer,Transaction> getTransactions(){
        Map<Integer,Transaction> TransactionMap = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT* FROM "+Table_Name1;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                Integer ID = cursor.getInt(0);
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd-MM-yyyy").parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String AccountNumber = cursor.getString(2);
                String expenseType = cursor.getString(3);
                ExpenseType ExpenseEN;
                if (expenseType == "EXPENSE") {
                    ExpenseEN = ExpenseType.EXPENSE;
                } else {
                    ExpenseEN = ExpenseType.INCOME;
                }
                double Amount = cursor.getDouble(4);

                Transaction transaction = new Transaction(date, AccountNumber, ExpenseEN, Amount);
                TransactionMap.put(ID,transaction);

            }while(cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return TransactionMap;
    }

    public void deleteAccount(String accountNo) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(Table_Name0,ACCOUNT_NUMBER+" =?", new String[]{accountNo});
        sqLiteDatabase.close();

    }
    public void updateAccount(Account account){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BALANCE,account.getBalance());
        sqLiteDatabase.update(Table_Name0,contentValues,ACCOUNT_NUMBER+"=?",new String[]{String.valueOf(account.getAccountNo())});
        sqLiteDatabase.close();
    }
}
