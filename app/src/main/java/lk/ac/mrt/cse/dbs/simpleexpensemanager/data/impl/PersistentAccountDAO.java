package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.Database.DatabaseHelper;

public class PersistentAccountDAO implements AccountDAO {
    private static Map<String,Account> accounts;
    private DatabaseHelper dbHelper;
    private Context context;

    public PersistentAccountDAO(Context context){
        this.accounts = new HashMap<>();
        this.context = context;
        this.dbHelper = DatabaseHelper.getInstance(context);

        // To add the current database into the Map
        currentAccountDatabase(dbHelper.getAccounts());
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
        if (accounts.containsKey(accountNo)){
            return accounts.get(accountNo);
        }
        throw new InvalidAccountException("Account"+accountNo+"is not in the Database");
    }

    @Override
    public void addAccount(Account account) {
        if (!accounts.containsKey(account.getAccountNo())) {
            dbHelper.addAccount(account);
            accounts.put(account.getAccountNo(), account);
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (accounts.containsKey(accountNo)){
            accounts.remove(accountNo);
            dbHelper.deleteAccount(accountNo);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)){
            String ExceptionMsg = "Account"+accountNo+"is invalid.";
            throw new InvalidAccountException(ExceptionMsg);
        }
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
        }
        dbHelper.updateAccount(account);
        accounts.put(accountNo,account);
    }
    public void currentAccountDatabase(List<Account> AccountList){
        for (Account account:AccountList){
            accounts.put(account.getAccountNo(),account);
        }
    }
}
