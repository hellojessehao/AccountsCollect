package com.zsh.app.accountscollect.model;

/**
 * Created by zhangshihao on 2017/9/20.
 */

public class Account {

    public String appName;
    public String account;
    public String password;
    public String boundPhone;
    public String boundEmail;
    public String userName;

    public Account(){}

    public Account(String appName,String account,String password,String boundPhone,String boundEmail){
        this(appName,account,password,boundPhone,boundEmail,null);
    }

    public Account(String appName,String account,String password,String boundPhone,String boundEmail,String userName){
        this.appName = appName;
        this.account = account;
        this.password = password;
        this.boundPhone = boundPhone;
        this.boundEmail = boundEmail;
        this.userName = userName;
    }
}
