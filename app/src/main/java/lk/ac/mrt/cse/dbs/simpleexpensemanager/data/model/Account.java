package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

/**
 *
 */
public class Account {
    private String accountNo;
    private String bankName;
    private String accountHolderName;
    private double balance;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Account(String accountNo, String bankName, String accountHolderName, double balance) {
        this.accountNo = accountNo;
        this.bankName = bankName;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }
}
