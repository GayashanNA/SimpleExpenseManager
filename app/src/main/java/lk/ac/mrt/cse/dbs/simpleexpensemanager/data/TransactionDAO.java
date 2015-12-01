package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 *
 */
public interface TransactionDAO {
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount);

    public List<Transaction> getAllTransactionLogs();

    public List<Transaction> getPaginatedTransactionLogs(int limit);
}
