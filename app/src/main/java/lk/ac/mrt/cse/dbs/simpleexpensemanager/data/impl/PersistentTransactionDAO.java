package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.util.Log;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    DatabaseHelper databaseHelper;

    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        boolean result = databaseHelper.logTransaction(date, accountNo, expenseType, amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return databaseHelper.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> allTransactions = databaseHelper.getAllTransactionLogs();
        int len = allTransactions.size();
        int lowerBound = len - limit;
        if(lowerBound < 0) lowerBound = 0;
        List<Transaction> result = allTransactions.subList(lowerBound, len);

        //Making the latest changes appear first in UI
        for (int i = 0, j = result.size() - 1; i<j; i++ ) {
            result.add(i, result.remove(j));
        }

        return result;
    }
}
