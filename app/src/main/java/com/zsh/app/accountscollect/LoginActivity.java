package com.zsh.app.accountscollect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.zsh.app.accountscollect.db.LoginDBHelper;

/**
 * Created by zhangshihao on 2017/9/22.
 */

public class LoginActivity extends BaseActivity {

    private EditText etLoginAccount;
    private EditText etLoginPsw;

    private SharedPreferences sp_ac;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sp_ac = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        init();
    }

    private void init(){
        etLoginAccount = (EditText) findViewById(R.id.et_login_account);
        etLoginPsw = (EditText) findViewById(R.id.et_login_psw);

        etLoginAccount.setText(sp_ac.getString("loged_username",null));
    }

    //onclick button @{
    public void loginin(View view){
        String strLoginAccount = etLoginAccount.getText().toString();
        String strLoginPsw = etLoginPsw.getText().toString();
        if(TextUtils.isEmpty(strLoginAccount)){
            showShortToast(this,"账号不能为空");
            return;
        }
        if(TextUtils.isEmpty(strLoginPsw)){
            showShortToast(this,"密码不能为空");
            return;
        }
        if(!LoginDBHelper.getInstance(this).isLoginAccountExist(strLoginAccount)){
            showShortToast(this,"用户名不存在");
            return;
        }
        if(LoginDBHelper.getInstance(this).isAccountPswMatch(strLoginAccount,strLoginPsw)){
            sp_ac.edit().putString("loged_username",strLoginAccount).commit();
            Intent toMain = new Intent(this,MainActivity.class);
            toMain.putExtra("username",strLoginAccount);
            startActivity(toMain);
            finish();
        }else{
            showShortToast(this,"密码不正确");
        }
    }

    public void changePsw(View view){
        startActivity(new Intent(this,ChangePswActivity.class));
    }

    public void register(View view){
        startActivity(new Intent(this,RegisterActivity.class));
    }
    //@}
}
