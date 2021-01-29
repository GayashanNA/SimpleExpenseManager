
package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseDAOFactoryAbstract;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.factory.DatabaseDAOFactory;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 *
 */
public class DatabaseDemoExpenseManager extends ExpenseManager {

    public DatabaseDemoExpenseManager() {
        setup();
    }

    @Override
    public void setup() {

        DatabaseDAOFactory databaseDAOFactory = DatabaseDAOFactoryAbstract.getDatabaseDAOFactory();

        TransactionDAO databaseTransactionDAO = databaseDAOFactory.getDatabaseAccountDAO();
        setTransactionsDAO(databaseTransactionDAO);

        AccountDAO databseAccountDAO = databaseDAOFactory.getDatabaseTransactionDAO();
        setAccountsDAO(databseAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
