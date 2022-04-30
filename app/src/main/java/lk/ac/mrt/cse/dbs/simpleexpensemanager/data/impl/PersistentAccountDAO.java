//package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
//
///**
// * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
// * used to store the account details temporarily in the memory.
// */
//public class PersistentAccountDAO implements AccountDAO {
//   /// private final Map<String, Account> accounts;
//
//    public PersistentAccountDAO() {
//        //this.accounts = new HashMap<>();
//    }
//
//    @Override
//    public List<String> getAccountNumbersList() {
//        return new ArrayList<>(accounts.keySet());
//    }
//
//    @Override
//    public List<Account> getAccountsList() {
//        return new ArrayList<>(accounts.values());
//    }
//
//    @Override
//    public Account getAccount(String accountNo) throws InvalidAccountException {
//        if (accounts.containsKey(accountNo)) {
//            return accounts.get(accountNo);
//        }
//        String msg = "Account " + accountNo + " is invalid.";
//        throw new InvalidAccountException(msg);
//    }
//
//    @Override
//    public void addAccount(Account account) {
//        accounts.put(account.getAccountNo(), account);
//    }
//
//    @Override
//    public void removeAccount(String accountNo) throws InvalidAccountException {
//        if (!accounts.containsKey(accountNo)) {
//            String msg = "Account " + accountNo + " is invalid.";
//            throw new InvalidAccountException(msg);
//        }
//        accounts.remove(accountNo);
//    }
//
//    @Override
//    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
//        if (!accounts.containsKey(accountNo)) {
//            String msg = "Account " + accountNo + " is invalid.";
//            throw new InvalidAccountException(msg);
//        }
//        Account account = accounts.get(accountNo);
//        // specific implementation based on the transaction type
//        switch (expenseType) {
//            case EXPENSE:
//                account.setBalance(account.getBalance() - amount);
//                break;
//            case INCOME:
//                account.setBalance(account.getBalance() + amount);
//                break;
//        }
//        accounts.put(accountNo, account);
//    }
//}