package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;


//import android.arch.persistence.db.SupportSQLiteOpenHelper;
//import android.arch.persistence.room.Database;
//import android.arch.persistence.room.DatabaseConfiguration;
//import android.arch.persistence.room.InvalidationTracker;
//import android.arch.persistence.room.RoomDatabase;
//import android.arch.persistence.room.TypeConverters;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

@Database(entities = {Account.class, ExpenseType.class, Transaction.class},version = 1,exportSchema = false)
@TypeConverters({DateConverter.class,ExpenseTypeConverter.class})
public class SQLIteDB extends RoomDatabase {


    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}


