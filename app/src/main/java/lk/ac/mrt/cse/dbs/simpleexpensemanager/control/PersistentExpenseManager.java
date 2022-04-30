package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;


import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;





public class PersistentExpenseManager extends ExpenseManager {
        private Context context;

    public PersistentExpenseManager(Context context) {
        this.context=context;
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO persistentTransactionDAO =Connection_.getInstance(context).getDatabase().getTransactionDAO();

        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = Connection_.getInstance(context).getDatabase().getAccountDAO();
        setAccountsDAO(persistentAccountDAO);

//        // dummy data
//        Account dummyAcct1 = new Account("12345AB", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
//       getAccountsDAO().addAccount(dummyAcct1);
//       getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
