package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.dbhelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class PersistentTransactionDAO implements TransactionDAO {
//    private final List<Transaction> transactions;
    public dbhelper dbhelp;

    public PersistentTransactionDAO(dbhelper dbhlp) {
//        transactions = new LinkedList<>();
        this.dbhelp=dbhlp;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
//        transactions.add(transaction);
        dbhelp.logtransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        return dbhelp.getalltransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> trans_list=dbhelp.getalltransactions();
        int size = trans_list.size();
        if (size <= limit) {
            return trans_list;
        }
        // return the last <code>limit</code> number of transaction logs
        return trans_list.subList(size - limit, size);
    }

}

