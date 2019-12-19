package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;




import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHandler databaseHandler;

    public PersistentAccountDAO(DatabaseHandler databaseHelper) {
        this.databaseHandler = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        //databaseHandler.GetAccountNO();
//

   }

    @Override
    public List<Account> getAccountsList() {

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        //databaseHandler.DeleteAccountDetails();
    }

    @Override
    public void addAccount(Account account) {
        databaseHandler.insertAccountDetails();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        databaseHandler.DeleteAccountDetails();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        databaseHandler.UpdateDetails()
}
