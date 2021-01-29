package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.factory.DatabaseDAOFactory;


public abstract class DatabaseDAOFactoryAbstract {
    public abstract DatabaseAccountDAO getDatabaseAccountDAO();
    public abstract DatabaseTransactionDAO getDatabaseTransactionDAO();

    public static DatabaseDAOFactory getDatabaseDAOFactory(){
        return new DatabaseDAOFactory();
    }

}