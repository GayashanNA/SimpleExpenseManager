package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteTransactionDAO implements TransactionDAO {
    private SQLiteHelper sqLiteHelper;

    public SQLiteTransactionDAO(Context context) {
        sqLiteHelper = SQLiteHelper.getInstance(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        //put
        cv.put(SQLiteHelper.TRANS_DATE, formatter.format(date));
        cv.put(SQLiteHelper.ACC_NO, String.valueOf(accountNo));
        cv.put(SQLiteHelper.EXPENSE_TYPE, String.valueOf(expenseType));
        cv.put(SQLiteHelper.AMOUNT, String.valueOf(amount));

        db.insert(SQLiteHelper.TRANSACTION_TABLE,null,cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteHelper.TRANSACTION_TABLE, null);
        return getToList(cursor);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteHelper.TRANSACTION_TABLE + " LIMIT "+limit, null);
        return getToList(cursor);

    }
    private List<Transaction> getToList(Cursor cursor){
        List<Transaction> transactionList = new LinkedList<>();
        try{
            SimpleDateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                String acc_no = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ACC_NO));
                String strDate = cursor.getString(cursor.getColumnIndex(SQLiteHelper.TRANS_DATE));
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.EXPENSE_TYPE)));
                double amount = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.AMOUNT));
                transactionList.add(new Transaction(formatter.parse(strDate),acc_no,expenseType,amount));
            }
        }
        catch(Exception e ){
            System.out.println(e.getMessage());
        }
        return transactionList;
    }
}
