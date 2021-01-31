package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseConnectionException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper implements DatabaseHandler {
    public final String DDL = "CREATE TABLE account(\n" +
            "\taccount_no TEXT(100) PRIMARY KEY,\n" +
            "   \tbank_name TEXT(100) NOT NULL,\n" +
            "\taccount_holder_name TEXT(100) NOT NULL,\n" +
            "\tbalance REAL NOT NULL);\n" +
            "CREATE TABLE transaction_account (\n" +
            "\taccount_no TEXT(100) NOT NULL,\n" +
            "\texpense_type TEXT NOT NULL CHECK (expense_type == \"EXPENSE\" OR expense_type == \"INCOME\"),\n" +
            "\tamount REAL NOT NULL,\n" +
            "\tdate NUMERIC NOT NULL,\n" +
            "\tFOREIGN KEY(account_no) REFERENCES account(account_no));";

    private SQLiteDatabase sql;

    public SQLiteDatabaseHandler(@Nullable Context context) {
        super(context, "expense_manager", null, 1);
        sql = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS account; DROP TABLE IF EXISTS transaction_account;");
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
}
