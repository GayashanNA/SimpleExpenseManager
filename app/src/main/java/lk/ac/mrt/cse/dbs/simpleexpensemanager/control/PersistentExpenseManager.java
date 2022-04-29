package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import java.util.Iterator;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    private DatabaseHelper sqlite;

    public PersistentExpenseManager(Context context) {
        this.sqlite = DatabaseHelper.getInstance(context);
        setContext(context);
        setup();
    }

    @Override
    public void setup(){
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(this.sqlite);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(this.sqlite);
        setAccountsDAO(persistentAccountDAO);

        Iterator iterator = sqlite.getAccountsList().iterator();

        while(iterator.hasNext()) {
            Account currentAccount = (Account) iterator.next();
            getAccountsDAO().addAccount(currentAccount);
        }
    }

}
