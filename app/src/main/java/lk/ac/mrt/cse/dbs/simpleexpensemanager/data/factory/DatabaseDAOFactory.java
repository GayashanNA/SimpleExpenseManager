package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.factory;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseTransactionDAO;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDAOFactory extends DatabaseDAOFactoryAbstract {
    private SQLiteDatabase mydatabase;

    public DatabaseDAOFactory() {
        mydatabase = openOrCreateDatabase("ExpenseManager", MODE_PRIVATE, null);
    }


    public static SQLiteDatabase getDatabase() {
        // Use DRIVER and DBURL to create a connection
        // Recommend connection pool implementation/usage
        return mydatabase;

    }

    @override
    public DatabaseAccountDAO getDatabaseAccountDAO(){
        return new DatabaseAccountDAO();
    }

    @override
    public DatabaseTransactionDAO getDatabaseTransactionDAO(){
        return new DatabaseTransactionDAO();
    }
}
