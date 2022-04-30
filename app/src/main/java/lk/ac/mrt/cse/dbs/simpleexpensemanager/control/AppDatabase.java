package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

@Database(entities = { Account.class,Transaction.class},version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AccountDAO getAccountDAO();
    public abstract TransactionDAO getTransactionDAO();


}
