package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.AMOUNT;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.DATE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.TRX_TBL;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseUtil.Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final DatabaseUtil dbUtil;
    private SQLiteDatabase db;

    public PersistentTransactionDAO(Context context) {
        dbUtil = new DatabaseUtil(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        db = dbUtil.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE,  new SimpleDateFormat("dd-MM-yyyy").format(date));
        values.put(ACCOUNT_NO, accountNo);
        values.put(Type, String.valueOf(expenseType));
        values.put(AMOUNT, amount);

        db.insert(TRX_TBL, null, values);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactions = new ArrayList<Transaction>();

        db = dbUtil.getReadableDatabase();

        String[] schema = {
                DATE,
                ACCOUNT_NO,
                Type,
                AMOUNT
        };

        Cursor cursor = db.query(
                TRX_TBL,
                schema,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {

            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(cursor.getString(cursor.getColumnIndex(DATE)));
            ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(Type)));
            Transaction transaction = new Transaction(date, cursor.getString(cursor.getColumnIndex(ACCOUNT_NO)),expenseType,cursor.getDouble(cursor.getColumnIndex(AMOUNT)));

            transactions.add(transaction);
        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {

        List<Transaction> transactions = new ArrayList<Transaction>();

        db = dbUtil.getReadableDatabase();

        String[] schema = {
                DATE,
                ACCOUNT_NO,
                Type,
                AMOUNT
        };

        Cursor cursor = db.query(
                TRX_TBL,
                schema,
                null,
                null,
                null,
                null,
                null
        );

        int length = cursor.getCount();

        while(cursor.moveToNext()) {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse( cursor.getString(cursor.getColumnIndex(DATE)));
            ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(Type)));
            Transaction transaction = new Transaction(date,cursor.getString(cursor.getColumnIndex(ACCOUNT_NO)),expenseType,cursor.getDouble(cursor.getColumnIndex(AMOUNT)));

            transactions.add(transaction);
        }

        if (length <= limit) {
            return transactions;
        }
        return transactions.subList(length - limit, length);


    }

}
