package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is a persistent implementation of TransactionDAO interface. All the
 * transaction logs are stored in a sqlite database.
 */
public class PersistentTransactionDAO implements TransactionDAO {

    private DatabaseHelper dbHelper;

    public PersistentTransactionDAO(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dbHelper.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbHelper.getTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = dbHelper.getTransactions();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }


}
