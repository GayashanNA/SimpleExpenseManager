package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseConnectionException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public interface DatabaseHandler {
    /**
     * Fetch all the accounts from the database.
     *
     * @return Map of all the accounts indexed by its account number
     */
    public Map<String, Account> fetchAllAccounts();

    /**
     * Insert an account to the database
     *
     * @param account - account to be added
     * @throws DatabaseConnectionException - if unable to update the database
     */
    public void addAccount(Account account) throws DatabaseConnectionException;

    /**
     * Removes the account from the database
     *
     * @param accountNo - account number given as int
     * @throws DatabaseConnectionException - if unable to update the database
     */
    public void removeAccount(String accountNo) throws DatabaseConnectionException;

    /**
     * Updates the account in the database
     *
     * @param account - account to be updated
     * @throws DatabaseConnectionException - if unable to update the database
     */
    public void updateAccount(Account account) throws DatabaseConnectionException;

    /**
     * Fetch all the transactions from the database.
     *
     * @return List of transactions
     */
    public List<Transaction> fetchAllTransactions() throws ParseException;

    /**
     * Insert a transaction to the database
     *
     * @param transaction - transaction to be added
     * @throws DatabaseConnectionException
     */
    public void addTransaction(Transaction transaction) throws DatabaseConnectionException;
}
