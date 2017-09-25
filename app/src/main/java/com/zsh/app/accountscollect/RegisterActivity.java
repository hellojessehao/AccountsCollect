package com.zsh.app.accountscollect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.zsh.app.accountscollect.db.LoginDBHelper;
import com.zsh.app.accountscollect.model.LoginAccount;

import java.util.List;

/**
 * Created by zhangshihao on 2017/9/22.
 */

public class RegisterActivity extends BaseActivity {

    private EditText etRegAccount;
    private EditText etRegPsw;
    private EditText etRegPsw2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    private void init(){
        etRegAccount = (EditText)findViewById(R.id.et_reg_account);
        etRegPsw = (EditText)findViewById(R.id.et_reg_psw);
        etRegPsw2 = (EditText)findViewById(R.id.et_reg_psw2);
    }

    //onclick button
    public void pushRegister(View view){
        String strAccount = etRegAccount.getText().toString();
        String strPsw = etRegPsw.getText().toString();
        String strPsw2 = etRegPsw2.getText().toString();
        if(TextUtils.isEmpty(strAccount)){
            showShortToast(this,"用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(strPsw) || TextUtils.isEmpty(strPsw2)){
            showShortToast(this,"密码不能为空");
            return;
        }
        if(LoginDBHelper.getInstance(this).isLoginAccountExist(strAccount)){
            showShortToast(this,"用户名已存在");
            etRegAccount.setText("");
            return;
        }
        if(!strPsw.equals(strPsw2)){
            showShortToast(this,"两次输入的密码不一致，请重新输入");
            etRegPsw2.setText("");
            return;
        }else{
            LoginAccount account = new LoginAccount();
            account.account = strAccount;
            account.password = strPsw;
            long result = LoginDBHelper.getInstance(this).insertData(account);
            if(result > 0){
                showLongToast(this,"恭喜您注册成功,请返回登陆界面登陆！");
            }else{
                showShortToast(this,"注册失败！");
            }
        }
    }

}
