package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> result = new ArrayList<>();
        List<Account> accList = databaseHelper.getAllAccounts();
        for (Account acc : accList) {
            result.add(acc.getAccountNo());
        }
        return result;
    }

    @Override
    public List<Account> getAccountsList() {
        return databaseHelper.getAllAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account result = databaseHelper.getAccount(accountNo);
        if (result == null) {
            throw new InvalidAccountException("Invalid account number!");
        } else return result;
    }

    @Override
    public void addAccount(Account account) {
        databaseHelper.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        boolean result = databaseHelper.removeAccount(accountNo);
        if (!result) throw new InvalidAccountException("Invalid account number!");
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        try {
            boolean result = databaseHelper.updateBalance(accountNo, expenseType, amount);
            if (!result) throw new InvalidAccountException("Invalid account number!");
        } catch (IllegalStateException ese) {
            Log.d("mydebug123", "ese");
        }
    }
}
