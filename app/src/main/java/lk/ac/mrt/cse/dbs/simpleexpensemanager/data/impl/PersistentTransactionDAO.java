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

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.Fields;

/**
 * Created by Eranga on 12/6/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {

    private Context context;

    //Constructor
    public PersistentTransactionDAO(Context context) {
        this.context = context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        DBHandler handler = DBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Save transaction details to the transaction_log table
        ContentValues values = new ContentValues();
        values.put(Fields.COLUMN_ACCOUNT_NO, accountNo);
        values.put(Fields.COLUMN_TRANSACTION_DATE, convertDateToString(date));
        values.put(Fields.COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(Fields.COLUMN_EXPENSE_TYPE, expenseType.toString());

        db.insert(Fields.TABLE_TRANSACTION_LOG,null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getPaginatedTransactionLogs(0);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        DBHandler handler = DBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to get details of all the transactions
        String query = "SELECT "+ Fields.COLUMN_ACCOUNT_NO + ", " +
                Fields.COLUMN_TRANSACTION_DATE + ", " +
                Fields.COLUMN_EXPENSE_TYPE+", " +
                Fields.COLUMN_TRANSACTION_AMOUNT +
                " FROM " + Fields.TABLE_TRANSACTION_LOG + " ORDER BY " + Fields.COLUMN_TRANSACTION_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Transaction> transactionLogs = new ArrayList<>();

        //Add the transaction details to a list
        while (cursor.moveToNext())
        {
            try {

                ExpenseType expenseType = null;
                if (cursor.getString(cursor.getColumnIndex(Fields.COLUMN_EXPENSE_TYPE)).equals(ExpenseType.INCOME.toString())) {
                    expenseType = ExpenseType.INCOME;
                }
                else{
                    expenseType = ExpenseType.EXPENSE;
                }

                String dateString = cursor.getString(cursor.getColumnIndex(Fields.COLUMN_TRANSACTION_DATE));
                Date date = convertStringToDate(dateString);

                Transaction tans = new Transaction(
                        date,
                        cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_NO)),
                        expenseType,
                        cursor.getDouble(cursor.getColumnIndex(Fields.COLUMN_TRANSACTION_AMOUNT)));

                transactionLogs.add(tans);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //Return the list of transactions
        return transactionLogs;
    }

    //Method to convert a date object to a string
    public static String convertDateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);
        return dateString;

    }

    //Method to convert a string to a date object
    public static Date convertStringToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date strDate = dateFormat.parse(date);
        return strDate;
    }
}
