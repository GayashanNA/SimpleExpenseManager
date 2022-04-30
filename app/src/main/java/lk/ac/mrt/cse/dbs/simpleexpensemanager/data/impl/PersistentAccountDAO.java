package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentAccountDAO implements AccountDAO {

    DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> result = new ArrayList<>();
        List<Account> accList = databaseHelper.getAllAcoounts();
        for (Account acc : accList) {
            result.add(acc.getAccountNo());
        }
        return result;
    }

    @Override
    public List<Account> getAccountsList() {
        return databaseHelper.getAllAcoounts();
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
        boolean result = databaseHelper.addAccount(account);
        if (result) Log.d("mydebug123", "Account added!");
        else Log.d("mydebug123", "Account couldn't be added!");
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        boolean result = databaseHelper.removeAccount(accountNo);
        if (result) Log.d("mydebug123", "Account DELETED!");
        else {
            Log.d("mydebug123", "Account couldn't be DELETED!");
            throw new InvalidAccountException("Invalid account number!");
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        try {
            boolean result = databaseHelper.updateBalance(accountNo, expenseType, amount);
            if (result) Log.d("mydebug123", "Account UPDATED!");
            else {
                Log.d("mydebug123", "Account couldn't be UPDATED!");
                throw new InvalidAccountException("Invalid account number!");
            }
        } catch (IllegalStateException ese) {
            Log.d("mydebug123", "ese");
        }
    }
}
