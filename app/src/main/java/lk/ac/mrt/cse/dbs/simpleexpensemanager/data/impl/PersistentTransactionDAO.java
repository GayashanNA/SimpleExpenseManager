package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.Database.DatabaseHelper;

public class PersistentTransactionDAO implements TransactionDAO {
    private DatabaseHelper dbHelper;
    private static Map<Integer,Transaction> transactionMap ;
    private Context context;

    public PersistentTransactionDAO(Context context){
        this.context = this.context;
        this.dbHelper = DatabaseHelper.getInstance(context);
        this.transactionMap = dbHelper.getTransactions();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dbHelper.addTransactions(transaction);
        transactionMap = dbHelper.getTransactions();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return new ArrayList<>(transactionMap.values());
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>(transactionMap.values());
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
