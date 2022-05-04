package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    private Context context ;
    @Override
    public void setup() throws ExpenseManagerException {
        AccountDAO persistentAccountDAO = new DBAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);

        TransactionDAO persistentTransactionDAO = new DBTransactionDAO(context);
        setTransactionsDAO(persistentTransactionDAO);

    }
    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context = context ;
        setup() ;
    }
}
