package com.zsh.app.accountscollect.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zsh.app.accountscollect.model.LoginAccount;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangshihao on 2017/9/22.
 */

public class LoginDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "login";
    public static final String ACCOUNT = "account";
    public static final String PSW = "password";

    private static LoginDBHelper mLoginDBHelper;

    public static LoginDBHelper getInstance(Context context){
        if(mLoginDBHelper == null){
            mLoginDBHelper = new LoginDBHelper(context);
        }
        return mLoginDBHelper;
    }

    public LoginDBHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL = "create table login(_id integer primary key autoincrement,account text,password text)";
        sqLiteDatabase.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertData(LoginAccount account){
        SQLiteDatabase db = mLoginDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT,account.account);
        values.put(PSW,account.password);
        return db.insert(TABLE_NAME,null,values);
    }

    public int deleteAllData(){
        SQLiteDatabase db = mLoginDBHelper.getWritableDatabase();
        return db.delete(TABLE_NAME,null,null);
    }

    public int updateData(LoginAccount account){
        SQLiteDatabase db = mLoginDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PSW,account.password);
        return db.update(TABLE_NAME,values,ACCOUNT+"=?",new String[]{account.account});
    }

    public Cursor queryAllData(){
        SQLiteDatabase db = mLoginDBHelper.getWritableDatabase();
        return db.query(TABLE_NAME,null,null,null,null,null,null);
    }

    public List<LoginAccount> getLoginAccountList(){
        Cursor cursor = queryAllData();
        if(cursor == null){
            return null;
        }
        if(cursor.getCount() == 0){
            return null;
        }
        List<LoginAccount> accounts = new ArrayList<>();
        while (cursor.moveToNext()){
            LoginAccount account = new LoginAccount();
            account.account = cursor.getString(cursor.getColumnIndex(ACCOUNT));
            account.password = cursor.getString(cursor.getColumnIndex(PSW));
            accounts.add(account);
        }
        return accounts;
    }

    public boolean isLoginAccountExist(String accountNum){
        List<LoginAccount> accounts = getLoginAccountList();
        if(accounts == null || accounts.size() == 0){
            return false;
        }
        for(LoginAccount account : accounts){
            if(accountNum.equals(account.account)){
                return true;
            }
        }
        return false;
    }

    public boolean isAccountPswMatch(String accountNum,String password){
        SQLiteDatabase db = mLoginDBHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{PSW},ACCOUNT+"=?",new String[]{accountNum},null,null,null);
        List<String> psws = new ArrayList<>();
        while(cursor.moveToNext()){
            psws.add(cursor.getString(cursor.getColumnIndex(PSW)));
        }
        for(String psw : psws){
            if(password.equals(psw)){
                return true;
            }
        }
        return false;
    }

}
