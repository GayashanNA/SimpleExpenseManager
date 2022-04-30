//package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
//
//
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
//
///**
// * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
// * transaction logs are stored in a LinkedList in memory.
//// */
//public class PersistentTransactionDAO implements TransactionDAO {
//    private final List<Transaction> transactions;
//
//    public PersistentTransactionDAO() {
//        transactions = new LinkedList<>();
//    }
//
//    @Override
//    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
//        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
//        transactions.add(transaction);
//    }
//
//    @Override
//    public List<Transaction> getAllTransactionLogs() {
//        return transactions;
//    }
//
//    @Override
//    public List<Transaction> getPaginatedTransactionLogs(int limit) {
//        int size = transactions.size();
//        if (size <= limit) {
//            return transactions;
//        }
//        // return the last <code>limit</code> number of transaction logs
//        return transactions.subList(size - limit, size);
//    }
//
//}
//
