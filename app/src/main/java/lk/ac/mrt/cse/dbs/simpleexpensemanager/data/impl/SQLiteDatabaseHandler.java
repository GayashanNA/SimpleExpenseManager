package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseConnectionException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper implements DatabaseHandler {
    private final String DDL_ACCOUNT  =  "CREATE TABLE account(" +
            "account_no TEXT(100) PRIMARY KEY, " +
            "bank_name TEXT(100) NOT NULL, " +
            "account_holder_name TEXT(100) NOT NULL, " +
            "balance REAL NOT NULL)";
    private final String DDL_TRANSACTION  =  "CREATE TABLE transaction_log(" +
            "date DATE NOT NULL, " +
            "account_no TEXT(100) NOT NULL, " +
            "expense_type TEXT NOT NULL CHECK (expense_type == \"EXPENSE\" OR expense_type == \"INCOME\"), " +
            "amount REAL NOT NULL, " +
            "FOREIGN KEY(account_no) REFERENCES account(account_no))";
    private final SQLiteDatabase sql;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static SQLiteDatabaseHandler instance = null;

    private SQLiteDatabaseHandler(Context context) {
        super(context, "180176R", null, 2);
        sql = this.getWritableDatabase();
    }

    public static SQLiteDatabaseHandler getInstance(Context context) {
        if (instance == null) instance = new SQLiteDatabaseHandler(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println("Helooooooooooooooooooooo");
        sqLiteDatabase.execSQL(DDL_ACCOUNT);
        sqLiteDatabase.execSQL(DDL_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS account");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transaction_account");
        onCreate(sqLiteDatabase);
    }

    @Override
    public Map<String, Account> fetchAllAccounts() {
        Cursor result = this.sql.rawQuery("SELECT * FROM account", null);
        Map<String, Account> accounts = new HashMap<String, Account>();
        while(result.moveToNext())
            accounts.put(result.getString(0),new Account(result.getString(0),result.getString(1),result.getString(2),result.getDouble(3)));
        return accounts;
    }

    @Override
    public void addAccount(Account account) throws DatabaseConnectionException {
        ContentValues cv = new ContentValues(4);
        cv.put("account_no", account.getAccountNo());
        cv.put("bank_name", account.getBankName());
        cv.put("account_holder_name", account.getAccountHolderName());
        cv.put("balance", account.getBalance());
        if (this.sql.insert("account",null,cv) == -1)
            throw new DatabaseConnectionException("Inserting account failed");
    }

    @Override
    public void removeAccount(String accountNo) throws DatabaseConnectionException {
        String[] whereArgs = {accountNo};
        if (this.sql.delete("account","account_no = ?", whereArgs) == 0)
            throw new DatabaseConnectionException("Deleting account failed");
    }

    @Override
    public void updateAccount(Account account) throws DatabaseConnectionException {
        String[] whereArgs = {account.getAccountNo()};
        ContentValues cv = new ContentValues(4);
        cv.put("account_no",account.getAccountNo());
        cv.put("bank_name",account.getBankName());
        cv.put("account_holder_name",account.getAccountHolderName());
        cv.put("balance",account.getBalance());
        if (sql.update("account",cv,"account_no = ?",whereArgs) == 0)
            throw new DatabaseConnectionException("Updating account failed");
    }

    @Override
    public List<Transaction> fetchAllTransactions() throws ParseException {
        Cursor result = this.sql.rawQuery("SELECT * FROM transaction_log", null);
        List<Transaction> transactions = new LinkedList<Transaction>();

        while(result.moveToNext())
            transactions.add(new Transaction(
                    DATE_FORMAT.parse(result.getString(0)),
                    result.getString(1),
                    ExpenseType.valueOf(result.getString(2)),
                    result.getDouble(3)));
        return transactions;
    }

    @Override
    public void addTransaction(Transaction transaction) throws DatabaseConnectionException {
        ContentValues cv = new ContentValues(4);
        cv.put("date",DATE_FORMAT.format(transaction.getDate()));
        cv.put("account_no", transaction.getAccountNo());
        cv.put("expense_type", transaction.getExpenseType().toString());
        cv.put("amount", transaction.getAmount());
        if (this.sql.insert("transaction_log",null, cv) == -1)
            throw new DatabaseConnectionException("Inserting transaction failed");
    }
}
