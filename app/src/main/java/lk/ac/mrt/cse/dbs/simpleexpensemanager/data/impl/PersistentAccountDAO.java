package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.dbhelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    public dbhelper dbhelp;

    public PersistentAccountDAO(dbhelper dbhlp){
        this.dbhelp=dbhlp;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return dbhelp.getaccnolist();
    }

    @Override
    public List<Account> getAccountsList() {
        return dbhelp.getacclist();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        List<Account> accounts=getAccountsList();
        for(Account acc:accounts){
            if (Objects.equals(acc.getAccountNo(), accountNo)) {
                return acc;
            }
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        dbhelp.addtoaccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbhelp.removeacc(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account acc_to_update=getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                dbhelp.updatebal(accountNo,-amount,acc_to_update);
                break;
            case INCOME:
                dbhelp.updatebal(accountNo,amount,acc_to_update);
                break;
        }

    }
}
