package com.zsh.app.accountscollect;

import android.app.Activity;
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
 * Created by zhangshihao on 2017/9/20.
 */

public class AddAccountActivity extends BaseActivity {

    private EditText etAppName;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etBoundPhone;
    private EditText etBoundEmail;

    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        if(intent != null){
            userName = intent.getStringExtra("username");
        }else{
            showShortToast(this,"未接收到用户名，请重新跳转");
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
    }

    public void commitDatas(View view){
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
        long result = AccountsDBHelper.getAccountsDBHelper(this).insertAccount(account);
        if(result >= 1){
            AlertDialog.Builder continueDialog = new AlertDialog.Builder(this)
                    .setMessage("账户添加成功，是否继续添加？")
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.putExtra("isneedfresh",true);
                            setResult(MainActivity.REQCODE_ADD,intent);
                            finish();
                        }
                    })
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            emptyEtContent();
                        }
                    });
            continueDialog.show();

        }else{
            showShortToast(this,"账户添加失败,请重新添加");
        }
    }

    private void emptyEtContent(){
        etAppName.setText("");
        etAccount.setText("");
        etPassword.setText("");
        etBoundPhone.setText("");
        etBoundEmail.setText("");
    }

}
