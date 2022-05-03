package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseManager extends SQLiteOpenHelper {

    public static final String ACCOUNT_TABLE = "ACCOUNT_TABLE";
    public static final String COLUMN_ACCOUNT_NO = "ACCOUNT_NO";
    public static final String COLUMN_BANK_NAME = "BANK_NAME";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
    public static final String COLUMN_BALANCE = "BALANCE";
    public static final String TRANSACTION_TABLE = "TRANSACTION_TABLE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_EXPENSE_TYPE = "EXPENSE_TYPE";
    public static final String COLUMN_AMOUNT = "AMOUNT";

    public DataBaseManager(@Nullable Context context) {
        super(context, "190140l_expenseManager.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            String createAccountTableStatement = "CREATE TABLE " + ACCOUNT_TABLE + " (" + COLUMN_ACCOUNT_NO + " TEXT, " + COLUMN_BANK_NAME + " TEXT, " + COLUMN_ACCOUNT_HOLDER_NAME + " TEXT, " + COLUMN_BALANCE + " REAL)";
            sqLiteDatabase.execSQL(createAccountTableStatement);
        }
        catch (Exception e){
            System.out.println("account table creation failed");
        }

        try{
            String createTransactionTableStatement = "CREATE TABLE " + TRANSACTION_TABLE + " (" + COLUMN_DATE + " TEXT, " + COLUMN_ACCOUNT_NO + " TEXT, " + COLUMN_EXPENSE_TYPE + " TEXT, " + COLUMN_AMOUNT + " REAL)";
            sqLiteDatabase.execSQL(createTransactionTableStatement);
        }
        catch (Exception e){
            System.out.println("TRANSACTION table creation failed");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
