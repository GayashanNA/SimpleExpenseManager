package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseConnectionException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final DatabaseHandler dbh;
    private final Map<String, Account> accounts;

    public PersistentAccountDAO (Context context) {
        dbh = SQLiteDatabaseHandler.getInstance(context);
        accounts = dbh.fetchAllAccounts();
    }

    @Override
    public List<String> getAccountNumbersList() {
        return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (accounts.containsKey(accountNo)) return accounts.get(accountNo);
        throw new InvalidAccountException("Account " + accountNo + " is invalid.");
    }

    @Override
    public void addAccount(Account account) throws DatabaseConnectionException {
        dbh.addAccount(account);
        accounts.put(account.getAccountNo(), account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException, DatabaseConnectionException {
        if (!accounts.containsKey(accountNo))
            throw new InvalidAccountException("Account " + accountNo + " is invalid.");
        dbh.removeAccount(accountNo);
        accounts.remove(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException, DatabaseConnectionException {
        if (!accounts.containsKey(accountNo))
            throw new InvalidAccountException("Account " + accountNo + " is invalid.");
        Account account = accounts.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        dbh.updateAccount(account);
        accounts.put(accountNo, account);
    }
}
