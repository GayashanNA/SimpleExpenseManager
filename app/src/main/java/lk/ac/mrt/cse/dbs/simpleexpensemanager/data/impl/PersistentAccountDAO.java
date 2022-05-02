package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */

public class PersistentAccountDAO implements AccountDAO{
    private final Map<String, Account> accounts;
    private final DBHelper db;
    public PersistentAccountDAO(DBHelper db) {
        this.accounts = new HashMap<>();
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor res = db.getAccountNum();
        List<String> output = new ArrayList<String>();
        if(res.getCount()==0){
            return output;
        }
        while(res.moveToNext()){
            String acc_no = res.getString(0);
            output.add(acc_no);

        }
        return output;
        //return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {

        Cursor res = db.getAccounts();
        List<Account> output = new ArrayList<Account>();
        if(res.getCount()==0){
            return output;
        }
        while(res.moveToNext()){
            String acc_no = res.getString(0);
            String bank=res.getString(1);;
            String owner= res.getString(2);;
            Double balance = res.getDouble(3);
            Account temp = new Account(acc_no,bank,owner,balance);
            output.add(temp);

        }
        return output;
        //return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor res = db.getAccount(accountNo);
        if (res.getCount()>0) {
            String acc_no = res.getString(0);
            String bank=res.getString(1);;
            String owner= res.getString(2);;
            Double balance = res.getDouble(3);
            Account temp = new Account(acc_no,bank,owner,balance);
            return temp;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        String bank = account.getBankName();
        String account_no =   account.getAccountNo();
        String holder = account.getAccountHolderName();
        Double balance = account.getBalance();
        db.insertAccount(account_no,bank,holder,balance);
        //accounts.put(account.getAccountNo(), account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        //accounts.remove(accountNo);
        db.deleteAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
      /* Cursor res = db.getAccount(accountNo);
        Account account;
        if (res.getCount()>0) {
            String acc_no = res.getString(0);
            String bank=res.getString(1);;
            String owner= res.getString(2);;
            Double balance = res.getDouble(3);
            account = new Account(acc_no,bank,owner,balance);
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        } */

      PersistentAccountDAO temp = new PersistentAccountDAO(db);
        Account account = temp.getAccount(accountNo);
        if(!(account instanceof Account)){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);

        }


        double balance1 = account.getBalance();
        switch (expenseType) {
            case EXPENSE:
                balance1 = account.getBalance() - amount;
                account.setBalance(balance1);
                break;
            case INCOME:
                balance1 = account.getBalance() + amount;
                account.setBalance(balance1);
                break;
        }
        accounts.put(accountNo, account);
       db.updateBalance(accountNo,balance1);
    }
}
