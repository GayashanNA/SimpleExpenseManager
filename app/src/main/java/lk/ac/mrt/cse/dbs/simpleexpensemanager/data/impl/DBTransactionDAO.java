package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.*;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DBTransactionDAO extends DatabaseHelper implements TransactionDAO {
    public DBTransactionDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(ACCOUNT_NO,accountNo);
        cv.put(AMOUNT,amount);
        cv.put(TRANSACTION_TYPE, String.valueOf(expenseType));
        cv.put(DATE, String.valueOf(date));

        db.insert(TRANSACTIONS_TABLE,null,cv);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db=this.getReadableDatabase();
        String querySQL = "SELECT * FROM " + TRANSACTIONS_TABLE + ";";
        Cursor cursor=db.rawQuery(querySQL,null);
        ExpenseType ex_type = null;



        List<Transaction> Tran_list= new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                Date date = null;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(4));
                    }
                } catch (Exception e) {
                }
                String acc_no= cursor.getString(1);
                double amount=cursor.getDouble(2);
                String tr_type=cursor.getString(3);
//                String date=cursor.getString(4);

                switch (tr_type){
                    case "EXPENSE":
                        ex_type=EXPENSE;
                    case "INCOME" :
                        ex_type=INCOME;
                }

                Transaction transaction=new Transaction(date,acc_no,ex_type,amount);
                Tran_list.add(transaction);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Tran_list;
    }


    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> allTransList=getAllTransactionLogs();

        int size=allTransList.size();
        // return the last <code>limit</code> number of transaction logs
        if(size>limit){
            return allTransList.subList(size - limit, size);}
        else{
            return allTransList;
        }
    }
}
