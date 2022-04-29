package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper sqlite;
    private final Map<String, Account> acc_map;

    public PersistentAccountDAO(DatabaseHelper sqlite) {
        this.sqlite = sqlite;
        this.acc_map = new HashMap<>();
    }

    @Override
    public List<String> getAccountNumbersList() {
        return sqlite.getAccountNumbersList();
    }

    @Override
    public List<Account> getAccountsList() {
        return sqlite.getAccountsList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = sqlite.getAccount(accountNo);
        if(account != null) {
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        sqlite.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if(!sqlite.removeAccount(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        sqlite.updateBalance(accountNo, expenseType.name(), amount);
    }

    @Override
    public double getAccountBalance(String accountNo) {
        return sqlite.getAccountBalance(accountNo);
    }

}
