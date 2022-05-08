package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;

@RunWith(AndroidJUnit4.class)
@MediumTest
//@FixMethodOrder(MethodSorters.DEFAULT.NAME_ASCENDING)
public class PersistentDBTest {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public PersistentDBTest(){
        DatabaseHelper db = new DatabaseHelper(ApplicationProvider.getApplicationContext());
        accountDAO = new PersistentAccountDAO(db);
        transactionDAO = new PersistentTransactionDAO(db);
    }

    public void addAccounts(){
        accountDAO.addAccount(new Account("111", "Star City", "Oliver Queen", 1000));
        accountDAO.addAccount(new Account("112", "Central City", "Barry Allen", 500));
        accountDAO.addAccount(new Account("113", "Gotham", "Bruce Wayne", 900));
        accountDAO.addAccount(new Account("114", "Metropolitan", "Clark Kent", 100));
        accountDAO.addAccount(new Account("115", "Coast City", "Hal Jordan", 50));
    }

    public boolean compareAccountVsAccountData(String accountNo, String bankName, String accountHolderName, double balance, Account account){
        return account.getAccountNo().equals(accountNo) && account.getAccountHolderName().equals(accountHolderName) && account.getBankName().equals(bankName) && account.getBalance()==balance;
    }

    @Test
    public void getAccount(){
        addAccounts();
        try{
            Account account = accountDAO.getAccount("111");
            assert  compareAccountVsAccountData("111", "Star City", "Oliver Queen", 1000, account);
        }catch (InvalidAccountException e){
            assert false;
        }
    }

    @Test
    public void getAccountsList(){
        addAccounts();
        List<Account> accountList = accountDAO.getAccountsList();
        String[] accountNumbers = {"111","112","113","114","115"};
        for (String accountNumber:accountNumbers) {
            boolean found = false;
            for (Account account:accountList) {
                if(account.getAccountNo().equals(accountNumber)){
                    found = true;
                    break;
                }
            }
            assert found;
        }
    }

    public void logTransactions(){
        Date date = new Date();
        transactionDAO.logTransaction(date,"111",ExpenseType.INCOME,500);
        transactionDAO.logTransaction(date,"111",ExpenseType.EXPENSE,200);
        transactionDAO.logTransaction(date,"111",ExpenseType.EXPENSE,300);
    }

    @Test
    public void getAllTransactions(){
        logTransactions();
        List<Transaction> transactionList =  transactionDAO.getAllTransactionLogs();
        Transaction[] transactions = {
                new Transaction(new Date(), "111", ExpenseType.INCOME, 500),
                new Transaction(new Date(), "111", ExpenseType.EXPENSE, 200),
                new Transaction(new Date(), "111", ExpenseType.EXPENSE, 300),
        };
        for (Transaction transaction:transactions) {
            boolean found = false;
            for (Transaction transaction1:transactionList) {
                if((transaction.getAccountNo().equals(transaction1.getAccountNo())) && (transaction.getAmount()==transaction1.getAmount()) && (transaction.getExpenseType()==transaction1.getExpenseType())){
                    found = true;
                    break;
                }
            }
            assert found;
        }
    }



}
