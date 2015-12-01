package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 *
 */
public class InMemoryTransactionDAO implements TransactionDAO {
    private Stack<Transaction> transactions;

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> getLogs() {
        List<Transaction> transactionsList = new ArrayList<>();
        transactionsList.addAll(transactions);
        return transactionsList;
    }

}
