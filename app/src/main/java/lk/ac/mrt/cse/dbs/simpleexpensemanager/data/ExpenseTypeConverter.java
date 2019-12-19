package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import androidx.room.TypeConverter;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class ExpenseTypeConverter {

    @TypeConverter
    public static String toString(ExpenseType expenseType) {

        return expenseType.getCode();
    }

    @TypeConverter
    public static ExpenseType toExpenseType(String string) {
        if (string.equals("INCOME")){
            return new ExpenseType("INCOME");
        }else {
            return new ExpenseType("EXPENSE");
        }

    }
}
