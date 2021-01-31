package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import java.text.ParseException;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseConnectionException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException, DatabaseConnectionException, ParseException {
        this.context = context;
        setup();
    }


    @Override
    public void setup() throws ExpenseManagerException, DatabaseConnectionException, ParseException {
        TransactionDAO transactionDAO = new PersistentTransactionDAO(context);
        setTransactionsDAO(transactionDAO);

        AccountDAO accountDAO = new PersistentAccountDAO(context);
        setAccountsDAO(accountDAO);
    }
}
