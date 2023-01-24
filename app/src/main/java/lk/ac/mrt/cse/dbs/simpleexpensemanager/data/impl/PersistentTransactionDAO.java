package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    SQLiteDatabase db;
    java.io.File filename = Constants.CONTEXT.getFilesDir();
    public PersistentTransactionDAO()
    {
        db = SQLiteDatabase.openOrCreateDatabase(filename.getAbsolutePath() + "/200592R.sqlite", null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Transactions(accountNo VARCHAR(50),expenseType VARCHAR(50)," +
                "amount NUMERIC(10,2), date_value Date);");
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        db.execSQL("INSERT INTO Transactions VALUES('"+accountNo+"','"+((expenseType==ExpenseType.INCOME)?"INCOME":"EXPENSE")
                +"','"+amount+"','"+date.toString()+"');");
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        @SuppressLint("Recycle") Cursor results = db.rawQuery("Select * from Transactions",null);
        results.moveToFirst();
        List<Transaction> result = new ArrayList<Transaction>();
        while(!results.isAfterLast())
        {
            result.add( new Transaction(new Date(results.getString(3)),results.getString(0),
                    ((results.getString(1)=="INCOME")?ExpenseType.INCOME:ExpenseType.EXPENSE),
                    Double.parseDouble(results.getString(2) ) ));

            results.moveToNext();
        }
        return result;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        @SuppressLint("Recycle") Cursor results = db.rawQuery("Select * from Transactions ORDER BY date_value LIMIT "+limit,
                null);
        results.moveToFirst();
        List<Transaction> result = new ArrayList<Transaction>();
        while(!results.isAfterLast())
        {
            result.add( new Transaction(new Date(results.getString(3)),results.getString(0),
                    ((results.getString(1)=="INCOME")?ExpenseType.INCOME:ExpenseType.EXPENSE),
                    Double.parseDouble(results.getString(2) ) ));

            results.moveToNext();
        }
        return result;
    }
}
