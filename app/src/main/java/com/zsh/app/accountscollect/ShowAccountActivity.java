package com.zsh.app.accountscollect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.zsh.app.accountscollect.db.AccountsDBHelper;
import com.zsh.app.accountscollect.model.Account;

/**
 * Created by zhangshihao on 2017/9/21.
 */

public class ShowAccountActivity extends BaseActivity {

    private EditText etAppName;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etBoundPhone;
    private EditText etBoundEmail;

    private String appName;
    private String account;
    private String password;
    private String boundphone;
    private String boundemail;
    private int accountId;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        if (intent != null){
            appName = intent.getStringExtra("appname");
            account = intent.getStringExtra("account");
            password = intent.getStringExtra("password");
            boundphone = intent.getStringExtra("boundphone");
            boundemail = intent.getStringExtra("boundemail");
            accountId = intent.getIntExtra("account_id",0);
            userName = intent.getStringExtra("username");
            logi("id = "+String.valueOf(accountId)+"\n username = "+userName);
        }else{
            showShortToast(this,"未接收到相关数据，请重新跳转");
            return;
        }
        init();
    }

    private void init(){
        etAppName = (EditText) findViewById(R.id.et_appname);
        etAccount = (EditText) findViewById(R.id.et_account);
        etPassword = (EditText) findViewById(R.id.et_password);
        etBoundPhone = (EditText) findViewById(R.id.et_bound_phonenum);
        etBoundEmail = (EditText) findViewById(R.id.et_bound_email);

        etAppName.setText(appName);
        etAccount.setText(account);
        etPassword.setText(password);
        etBoundPhone.setText(boundphone);
        etBoundEmail.setText(boundemail);
    }

    public void updateData(View view){
        Account account = new Account();
        if(etAppName.getText() != null){
            account.appName = etAppName.getText().toString();
        }else{
            account.appName = null;
        }
        if(etAccount.getText() != null){
            account.account = etAccount.getText().toString();
        }else{
            account.account = null;
        }
        if(etPassword.getText() != null){
            account.password = etPassword.getText().toString();
        }else{
            account.password = null;
        }
        if(etBoundPhone.getText() != null){
            account.boundPhone = etBoundPhone.getText().toString();
        }else{
            account.boundPhone = null;
        }
        if(etBoundEmail.getText() != null){
            account.boundEmail = etBoundEmail.getText().toString();
        }else{
            account.boundEmail = null;
        }
        account.userName = userName;
        try {
            AccountsDBHelper.getAccountsDBHelper(this).updateAccountById(account, accountId);
            showShortToast(this,"账户修改成功");
            Intent intent = new Intent();
            intent.putExtra("isneedfresh",true);
            setResult(MainActivity.REQCODE_UPDATE,intent);
            finish();
        }catch (Exception e){
            showShortToast(this,"账户修改失败,请稍后重试");
        }
    }

}
