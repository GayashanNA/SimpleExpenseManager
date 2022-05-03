package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    public PersistentExpenseManager(Context context) {
        setup(context);
    }

    @Override
    public void setup() throws ExpenseManagerException {
    }

    @Override
    public void setup(Context context) {
        setAccountsDAO(new SQLiteAccountDAO(context));
        setTransactionsDAO(new SQLiteTransactionDAO(context));
    }
}
