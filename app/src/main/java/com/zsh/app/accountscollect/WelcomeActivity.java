package com.zsh.app.accountscollect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.zsh.app.accountscollect.db.AccountsDBHelper;

public class WelcomeActivity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (!sp.getBoolean("is_first_enter",true)){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

    }

    public void gointoMain(View view){
        sp.edit().putBoolean("is_first_enter",false).commit();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

}
