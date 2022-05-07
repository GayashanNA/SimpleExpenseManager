package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

import static android.content.ContentValues.TAG;

@RunWith(RobolectricTestRunner.class)
public class DatabaseHelperUnitTest {

    private DatabaseHelper sqlite;

    public DatabaseHelperUnitTest() {
    }

    @Before
    public void initialize() {
        this.sqlite = DatabaseHelper.getInstance(ApplicationProvider.getApplicationContext());
        SQLiteDatabase db = this.sqlite.getWritableDatabase();
        db.execSQL("DELETE FROM trans");
        db.execSQL("DELETE FROM account");
        db.close();
    }

    @After
    public void finishTesting() {
        resetSingleton(this.sqlite.getClass(), "sInstance");
    }
    private void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    private Account getAccountPrivate(String accountNo) {
        Account account;
        String ACCOUNT_SELECT_QUERY = "SELECT accountNo, bankName, holderName, balance FROM account WHERE accountNo=? and isActive=1";
        SQLiteDatabase db = sqlite.getReadableDatabase();
        Cursor cursor = db.rawQuery(ACCOUNT_SELECT_QUERY, new String[]{accountNo});

        try {
            if(cursor.getCount() == 0) {
                return null;
            }
            if(cursor.moveToFirst()) {
                account = new Account(cursor.getString(cursor.getColumnIndex("accountNo")), cursor.getString(cursor.getColumnIndex("bankName")), cursor.getString(cursor.getColumnIndex("holderName")), cursor.getDouble(cursor.getColumnIndex("balance")));
                return account;
            }
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    @Test
    public void testTest() {
    }

    @Test
    public void addAccount() {

        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        sqlite.addAccount(dummyAcct1);
        sqlite.addAccount(dummyAcct2);

        Account dummyAcctRec1 = getAccountPrivate(dummyAcct1.getAccountNo());
        Account dummyAcctRec2 = getAccountPrivate(dummyAcct2.getAccountNo());
        org.junit.Assert.assertNotNull(dummyAcctRec1);
        org.junit.Assert.assertNotNull(dummyAcctRec2);

        org.junit.Assert.assertEquals(dummyAcct1.getAccountNo(), dummyAcctRec1.getAccountNo());
        org.junit.Assert.assertEquals(dummyAcct2.getAccountNo(), dummyAcctRec2.getAccountNo());

    }

    @Test
    public void getAccountNumbersList() {
        String[] dummyAccountList = {"12345A", "78945Z"};
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        sqlite.addAccount(dummyAcct1);
        sqlite.addAccount(dummyAcct2);
        List<String> retrievedValue = sqlite.getAccountNumbersList();
        String[] outputList = new String[2];
        for (int i=0; retrievedValue.size() > i; i++) {
            outputList[i] = retrievedValue.get(i);
        }
        org.junit.Assert.assertArrayEquals(dummyAccountList, outputList);
    }

    @Test
    public void getAccountsList() {
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        sqlite.addAccount(dummyAcct1);
        sqlite.addAccount(dummyAcct2);

        List<Account> dummyAccounts = new ArrayList<>();
        dummyAccounts.add(dummyAcct1);
        dummyAccounts.add(dummyAcct2);

        List<Account> retrievedAccounts = sqlite.getAccountsList();

        for(int i=0; i<retrievedAccounts.size(); i++) {
            org.junit.Assert.assertEquals(dummyAccounts.get(i).getAccountNo(),retrievedAccounts.get(i).getAccountNo());
        }
    }

    @Test
    public void getAccount() {
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        sqlite.addAccount(dummyAcct1);
        sqlite.addAccount(dummyAcct2);

        Account retrievedAccount1 = sqlite.getAccount("12345A");
        Assert.assertNotNull(retrievedAccount1);
        Assert.assertEquals(dummyAcct1.getAccountNo(), retrievedAccount1.getAccountNo());

        Account retrievedAccount2 = sqlite.getAccount("78945Z");
        Assert.assertNotNull(retrievedAccount2);
        Assert.assertEquals(dummyAcct2.getAccountNo(), retrievedAccount2.getAccountNo());
    }

    @Test
    public void removeAccount() {
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        sqlite.addAccount(dummyAcct1);
        sqlite.addAccount(dummyAcct2);

        sqlite.removeAccount("12345A");
        Assert.assertNull(getAccountPrivate("12345A"));
        sqlite.removeAccount("78945Z");
        Assert.assertNull(getAccountPrivate("12345A"));
    }

    @Test
    public void getAccountBalance() {
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        sqlite.addAccount(dummyAcct1);
        sqlite.addAccount(dummyAcct2);

        List<Account> dummyAccount = new ArrayList<>();
        Account ret;
        dummyAccount.add(dummyAcct1);
        dummyAccount.add(dummyAcct2);
        for(int i=0; i<dummyAccount.size(); i++) {
            ret = dummyAccount.get(i);
            Assert.assertEquals(ret.getBalance(), sqlite.getAccountBalance(ret.getAccountNo()), 0.9);
        }
    }
}