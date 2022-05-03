package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBaseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an persistent implementation of TransactionDAO interface.
 */

public class PersistentTransactionDAO extends DataBaseManager implements TransactionDAO {

    public PersistentTransactionDAO(Context context) {
        super(context);
    }

    @Override
    public boolean logTransaction(Date date, String accountNumber, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        cv.put(ACCOUNT_NUMBER, accountNumber);

        cv.put(DATE, formattedDate.toString());
        cv.put(TYPE, expenseType == ExpenseType.EXPENSE ? 0 : 1);
        cv.put(AMOUNT, amount);

        if (db.insert(TRANSACTION_TABLE, null, cv) == -1 )
            return false;
        else
            return true;
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        String queryString = "SELECT * from TRANSACTION_TABLE";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    Date date =new SimpleDateFormat("dd-MM-yyyy").parse(cursor.getString(1));
                    String accountNo = cursor.getString(2);
                    String type = cursor.getString(3);
                    ExpenseType expenseType = null;

                    if (type.equals("EXPENSE")) {
                        expenseType = ExpenseType.EXPENSE;
                    }

                    else if (type.equals("INCOME")) {
                        expenseType = ExpenseType.INCOME;
                    }

                    double amount = cursor.getDouble(4);
                    Transaction newTransaction = new Transaction(date, accountNo, expenseType, amount);
                    transactions.add(newTransaction);
//                    System.out.println(newTransaction);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return  transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String queryString = "SELECT DATE,ACCOUNT_NUMBER,TYPE,AMOUNT from TRANSACTION_TABLE";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            int i =1;
            do {
                try {
                    Date date =new SimpleDateFormat("dd-MM-yyyy").parse(cursor.getString(0));
                    String accountNumber = cursor.getString(1);
                    int type = cursor.getInt(2);
                    ExpenseType expenseType = null;

                    if (type == 0) {
                        expenseType = ExpenseType.EXPENSE;
                    }

                    else if (type == 1) {
                        expenseType = ExpenseType.INCOME;
                    }

                    double amount = cursor.getDouble(3);
                    Transaction newTransaction = new Transaction(date, accountNumber, expenseType, amount);
                    transactions.add(newTransaction);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                i++;

            } while (cursor.moveToNext() && i < limit);
        }
        cursor.close();
        db.close();
        return  transactions;
    }
}