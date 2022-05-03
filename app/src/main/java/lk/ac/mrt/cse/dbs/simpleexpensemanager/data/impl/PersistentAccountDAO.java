package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends DataBaseManager implements AccountDAO {

    public PersistentAccountDAO(Context context){
        super(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();
        String queryStr = "SELECT " + COLUMN_ACCOUNT_NO + " FROM " + ACCOUNT_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryStr, null);

        if (cursor.moveToFirst()){
            do{
                String account_no = cursor.getString(0);
                accountNumbersList.add(account_no);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        String queryStr = "SELECT * FROM " + ACCOUNT_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryStr, null);

        if (cursor.moveToFirst()){
            do{
                String account_no = cursor.getString(0);
                String bank_name = cursor.getString(1);
                String account_holder_name = cursor.getString(2);
                double balance = cursor.getDouble(3);
                Account account = new Account(account_no, bank_name, account_holder_name, balance);
                accountList.add(account);

            }while (cursor.moveToNext());
        }
        //nothing to do

        cursor.close();
        sqLiteDatabase.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String queryStr = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NO + " = " + accountNo;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryStr, null);

        if (cursor.moveToFirst()){
            String account_no = cursor.getString(0);
            String bank_name = cursor.getString(1);
            String account_holder_name = cursor.getString(2);
            double balance = cursor.getDouble(3);
            Account account = new Account(account_no, bank_name, account_holder_name, balance);
            cursor.close();
            sqLiteDatabase.close();
            return account;
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NO, account.getAccountNo());
        cv.put(COLUMN_BANK_NAME, account.getBankName());
        cv.put(COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        cv.put(COLUMN_BALANCE, account.getBalance());

        sqLiteDatabase.insert(ACCOUNT_TABLE, null, cv);

        sqLiteDatabase.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String queryStr = "DELETE FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NO + " = " + accountNo;
        Cursor cursor = sqLiteDatabase.rawQuery(queryStr, null);
        if(cursor.moveToFirst()){
            //do nothing
            cursor.close();
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String queryStr = "SELECT " + COLUMN_BALANCE + " FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NO + " = " + accountNo;
        Cursor cursor = sqLiteDatabase.rawQuery(queryStr, null);
        if(!cursor.moveToFirst()){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BALANCE));

        switch (expenseType) {
            case EXPENSE:
                balance = balance - amount;
                break;
            case INCOME:
                balance = balance + amount;
                break;
        }
        cursor.close();
        sqLiteDatabase.close();

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BALANCE, balance);
        String[] selectArgs = {accountNo};
        sqLiteDatabase.update(ACCOUNT_TABLE, cv, COLUMN_ACCOUNT_NO+ " = ?", selectArgs);
        sqLiteDatabase.close();
    }
}
