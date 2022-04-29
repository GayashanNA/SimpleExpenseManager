package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    private static final int DB_VERSION = 2;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    public PersistentTransactionDAO(Context context, String dbname) {
        super(context, dbname, null, DB_VERSION);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO transactions(date, account_number, expense_type, amount) VALUES (?, ?, ?, ?)", new Object[]{dateFormat.format(date), accountNo, expenseType.name(), amount});
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionLogs = new LinkedList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM transactions", null);
        if(cursor.moveToFirst()){
            do{
                try {
                    transactionLogs.add(new Transaction(dateFormat.parse(cursor.getString(1)), cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)), cursor.getFloat(4) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return transactionLogs;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS transactions(transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, date DATETIME NOT NULL, account_number VARCHAR(15) NOT NULL, expense_type VARCHAR(7) NOT NULL check(expense_type in ('EXPENSE', 'INCOME')), amount FLOAT(10,2) )";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
