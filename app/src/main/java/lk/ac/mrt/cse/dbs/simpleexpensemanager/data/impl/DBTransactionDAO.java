package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

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
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DBTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd") ;
    public DBTransactionDAO(@Nullable Context context) {
        super(context,"190081F.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String dateString = formatter.format(date);
        String expType = expenseType.name() ;
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;

        cv.put(Constants.ACCOUNT_NO_COL, accountNo);
        cv.put(Constants.EXPENSE_TYPE_COL, expType) ;
        cv.put(Constants.AMOUNT_COL, amount) ;
        cv.put(Constants.RECORD_DATE_COL, dateString) ;

        db.insert(Constants.TRANS_RECORD_TABLE, null, cv) ;
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactionList = new ArrayList<>() ;
        String queryStmt = "SELECT * FROM " + Constants.TRANS_RECORD_TABLE + " ;";

        SQLiteDatabase db = this.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(queryStmt,null) ;

        if (cursor.moveToFirst()){
            do {
                String accountNo = cursor.getString(1);
                ExpenseType type = ExpenseType.valueOf(cursor.getString(2)) ;
                Double amount = cursor.getDouble(3);
                String dateString = cursor.getString(4) ;
                Date dateObj = formatter.parse(dateString) ;

                Transaction transaction = new Transaction(dateObj,accountNo,type,amount) ;
                transactionList.add(transaction) ;

            }while (cursor.moveToNext()) ;
        }else {
            //void
        }

        return transactionList ;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List <Transaction> fullList ;
        try {
            fullList = getAllTransactionLogs() ;
            int fullSize = fullList.size() ;
            if (fullSize<=limit) {
                return fullList ;
            }
            return fullList.subList(fullSize - limit, fullSize) ;
        } catch (ParseException e) {
            e.printStackTrace();
            fullList = new ArrayList<>() ;
            return fullList ;
        }
    }
}
