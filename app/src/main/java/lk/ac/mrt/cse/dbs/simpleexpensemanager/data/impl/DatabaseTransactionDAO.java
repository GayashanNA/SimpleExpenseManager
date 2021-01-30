
package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;


/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class DatabaseTransactionDAO implements TransactionDAO {
    private SQLiteDatabase db;

    public DatabaseTransactionDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        db.execSQL("INSERT INTO transaction_account (date, account_no, expense_type, amount) VALUES ("+date+", '"+accountNo+"', '"+expenseType+"', "+amount+")");
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Cursor cursor = db.rawQuery("SELECT * FROM transaction_account;",null);

        if (!cursor.moveToFirst()) {
            return transactions;
        }

        Transaction transaction;

        do {

            transaction = new Transaction(new Date(cursor.getLong(1)), cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)), cursor.getDouble(4));
            transactions.add(transaction);

        } while (cursor.moveToNext());

        return transactions;
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

}
