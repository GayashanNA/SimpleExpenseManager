package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private final List<Transaction> transactions;
    private DBHelper db;

    public PersistentTransactionDAO(DBHelper db) {
        transactions = new LinkedList<>();
        this.db = db;

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String strDate = dateFormat.format(date);
        db.insertTransaction(strDate,accountNo,expenseType,amount);
        //Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        //transactions.add(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {

        Cursor res = db.getTransactions();
        List<Transaction> output = new ArrayList<Transaction>();
        if(res.getCount()==0){
            return output;
        }
        while(res.moveToNext()){
            ExpenseType type;
            String acc_no=res.getString(1);;
            String date_temp = res.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
            Date date =formatter.parse(date_temp);
            Double amount= res.getDouble(3);
            String type_temp = res.getString(4);
            Log.d("myTag", type_temp);

            type = ExpenseType.valueOf(type_temp);
            Transaction temp = new Transaction(date,acc_no,type,amount);
            output.add(temp);

        }
        return output;
        //return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {

        Cursor res = db.getLimitedTransactions(limit);
        List<Transaction> output = new ArrayList<Transaction>();
        if(res.getCount()==0){
            return output;
        }
        while(res.moveToNext()){
            ExpenseType type;
            String acc_no=res.getString(1);;
            String date_temp = res.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
            Date date =formatter.parse(date_temp);
            Double amount= res.getDouble(3);
            String type_temp = res.getString(4);
            type = ExpenseType.valueOf(type_temp);
            Transaction temp = new Transaction(date,acc_no,type,amount);
            output.add(temp);

        }
        return output;
        //return transactions;

        /*int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);*/
    }
}
