package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBTransactionDAO;

public class DBExpenseManager extends ExpenseManager {
    private SQLiteDB db;
    public DBExpenseManager(SQLiteDB db) {
        this.db = db;
        setup();
    }


    @Override
    public void setup() {

        TransactionDAO persistentTransactionDAO = new DBTransactionDAO(db);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new DBAccountDAO(db);
        setAccountsDAO(persistentAccountDAO);

    }
}
