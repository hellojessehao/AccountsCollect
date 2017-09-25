package com.zsh.app.accountscollect.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zsh.app.accountscollect.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshihao on 2017/9/20.
 */

public class AccountsDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String APPNAME = "appname";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String BOUNDPHONE = "boundphone";
    public static final String BOUNDEMAIL = "boundemail";
    public static final String USERNAME = "username";

    private static AccountsDBHelper accountsDBHelper;

    public static AccountsDBHelper getAccountsDBHelper(Context context){
        if(accountsDBHelper == null){
            accountsDBHelper = new AccountsDBHelper(context);
        }
        return accountsDBHelper;
    }

    public AccountsDBHelper(Context context) {
        super(context, "accounts.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL = "create table accounts(_id integer primary key autoincrement," +
                "appname text,account text,password text,boundphone text,boundemail text,username text)";
        sqLiteDatabase.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {

    }

    public long insertAccount(Account account){
        SQLiteDatabase db = accountsDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APPNAME,account.appName);
        values.put(ACCOUNT,account.account);
        values.put(PASSWORD,account.password);
        values.put(BOUNDPHONE,account.boundPhone);
        values.put(BOUNDEMAIL,account.boundEmail);
        values.put(USERNAME,account.userName);
        return db.insert(TABLE_ACCOUNTS,null,values);
    }

    public int deleteAccount(Account account){
        SQLiteDatabase db = accountsDBHelper.getWritableDatabase();
        return db.delete(TABLE_ACCOUNTS,APPNAME+"= ? and "+ACCOUNT+"= ? and "
                +PASSWORD+"= ? and "+BOUNDPHONE+"= ? and "+BOUNDEMAIL+"=? and "+USERNAME+"=?"
                ,new String[]{account.appName,account.account,
                        account.password,account.boundPhone,account.boundEmail,account.userName});
    }

    public void updateAccountById(Account account,int _id){
        SQLiteDatabase db = accountsDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APPNAME,account.appName);
        values.put(ACCOUNT,account.account);
        values.put(PASSWORD,account.password);
        values.put(BOUNDPHONE,account.boundPhone);
        values.put(BOUNDEMAIL,account.boundEmail);
        values.put(USERNAME,account.userName);
        db.update(TABLE_ACCOUNTS,values,"_id = ?",new String[]{String.valueOf(_id)});
    }

    public Cursor queryAllAccount(){
        SQLiteDatabase db = accountsDBHelper.getWritableDatabase();
        return db.query(TABLE_ACCOUNTS,null,null,null,null,null,"_id");
    }

    public Cursor queryAllAccountByUserName(String userName){
        SQLiteDatabase db = accountsDBHelper.getWritableDatabase();
        return db.query(TABLE_ACCOUNTS,null,USERNAME+"=?",new String[]{userName},null,null,"_id");
    }

    public Cursor queryAccount(Account account){
        SQLiteDatabase db = accountsDBHelper.getReadableDatabase();
        Log.d("zhangshihao","appName = "+account.appName+"\n account = "+account.account+
        "\n password = "+account.password+"\n boundphone = "+account.boundPhone+"\n boundemail = "+
        account.boundEmail);
        return db.query(TABLE_ACCOUNTS,null,
                APPNAME+"=? and "+ACCOUNT+"=? and "+USERNAME+"=?",
                new String[]{account.appName,account.account,account.userName},null,null,null);
    }

}
