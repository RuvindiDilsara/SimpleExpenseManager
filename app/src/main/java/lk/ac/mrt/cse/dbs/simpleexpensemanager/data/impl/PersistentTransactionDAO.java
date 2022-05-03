package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends DataBaseManager implements TransactionDAO {

    public PersistentTransactionDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, strDate);
        cv.put(COLUMN_ACCOUNT_NO, accountNo);
        cv.put(COLUMN_EXPENSE_TYPE, String.valueOf(expenseType));
        cv.put(COLUMN_AMOUNT, amount);
        sqLiteDatabase.insert(TRANSACTION_TABLE, null, cv);
        sqLiteDatabase.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactionsList = new ArrayList<>();
        String queryStr = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryStr, null);

        if (cursor.moveToFirst()){
            do{
                String date = cursor.getString(0);
                String account_no = cursor.getString(1);
                String expense_type = cursor.getString(2);
                double amount = cursor.getDouble(3);
//                SimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(date);
                Transaction transaction = new Transaction(date1, account_no, ExpenseType.valueOf(expense_type), amount);
                transactionsList.add(transaction);

            }while (cursor.moveToNext());
        }
        //nothing to do

        cursor.close();
        sqLiteDatabase.close();
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List transactions = this.getAllTransactionLogs();
        int size = transactions.size();
        if(size<= limit){
            return transactions;
        }
        return transactions.subList(size-limit, size);
    }
}
