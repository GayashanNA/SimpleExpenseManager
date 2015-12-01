package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import java.util.Date;

/**
 *
 */
public class Transaction {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private String accountNo;
    private ExpenseType expenseType;
    private double amount;

    public Transaction(Date date, String accountNo,
                       ExpenseType expenseType, double amount) {
        this.date = date;
        this.accountNo = accountNo;
        this.expenseType = expenseType;
        this.amount = amount;
    }
}
