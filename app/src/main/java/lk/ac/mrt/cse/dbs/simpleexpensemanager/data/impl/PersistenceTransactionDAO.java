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

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.SqliteHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistenceTransactionDAO implements TransactionDAO {
    private Context context;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PersistenceTransactionDAO(Context context) {
        this.context = context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no", accountNo);

        contentValues.put(sqliteHandler.COLUMN_DATE, dateFormat.format(date));
        contentValues.put(sqliteHandler.COLUMN_TYPE, String.valueOf(expenseType));
        contentValues.put(sqliteHandler.COLUMN_AMOUNT, amount);
        sqLiteDatabase.insert(sqliteHandler.TABLE_TRANSACTIONS, null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_TRANSACTIONS + " ;", null);
        if (cursor.getCount() > 0){
            List<Transaction> transactions = new ArrayList<>();
            while (cursor.moveToNext()){
                String account_no = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_NO));
                String date_str = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_DATE));
                String type = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_TYPE));
                double amount = cursor.getDouble(cursor.getColumnIndex(sqliteHandler.COLUMN_AMOUNT));

                ExpenseType expense_type = null;

                if (ExpenseType.EXPENSE.name().equals(type)){
                    expense_type = ExpenseType.EXPENSE;
                }else {
                    expense_type = ExpenseType.INCOME;
                }

                try{
                    Date date = dateFormat.parse(date_str);
                    transactions.add(new Transaction(date, account_no, expense_type, amount));
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
            return transactions;
        }else {
            return  new ArrayList<Transaction>();
        }
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SqliteHandler sqliteHandler = SqliteHandler.getInstanceDB(context);
        SQLiteDatabase sqLiteDatabase = sqliteHandler.getWritableDatabase();
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_TRANSACTIONS + " LIMIT " + limit + " ;", null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                String account_no = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_NO));
                String date_str = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_DATE));
                String type = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_TYPE));
                double amount = cursor.getDouble(cursor.getColumnIndex(sqliteHandler.COLUMN_AMOUNT));
                ExpenseType expense_type = ExpenseType.valueOf(type);

                Date date = null;
                try {
                    date = dateFormat.parse(date_str);
                    transactions.add(new Transaction(date, account_no, expense_type, amount));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            return transactions;
        }else {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqliteHandler.TABLE_TRANSACTIONS + " ;", null);
            if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String account_no = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_ACCOUNT_NO));
                    String date_str = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_DATE));
                    String type = cursor.getString(cursor.getColumnIndex(sqliteHandler.COLUMN_TYPE));
                    double amount = cursor.getDouble(cursor.getColumnIndex(sqliteHandler.COLUMN_AMOUNT));

                    ExpenseType expense_type = null;

                    if (ExpenseType.EXPENSE.name().equals(type)){
                        expense_type = ExpenseType.EXPENSE;
                    }else {
                        expense_type = ExpenseType.INCOME;
                    }

                    try {
                        Date date = dateFormat.parse(date_str);
                        transactions.add(new Transaction(date, account_no, expense_type, amount));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
            return transactions;

        }
    }
}
