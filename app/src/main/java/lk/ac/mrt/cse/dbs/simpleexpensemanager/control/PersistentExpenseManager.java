package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


public class PersistentExpenseManager extends  ExpenseManager{

    private Context context;

    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating initiating TransactionDAO and AccountDAO objects for the persistent implementation ***/

        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(databaseHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO inMemoryAccountDAO = new PersistentAccountDAO(databaseHelper);
        setAccountsDAO(inMemoryAccountDAO);


        /*** End ***/
    }


}
