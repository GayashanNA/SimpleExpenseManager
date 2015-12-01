package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 *
 */
public interface AccountDAO {
    public List<String> getAccountNumbersList();

    public List<Account> getAccountsList();

    public Account getAccount(String accountNo) throws InvalidAccountException;

    public void addAccount(Account account);

    public void removeAccount(String accountNo) throws InvalidAccountException;

    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException;

}
