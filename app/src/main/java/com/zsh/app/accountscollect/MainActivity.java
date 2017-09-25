package com.zsh.app.accountscollect;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zsh.app.accountscollect.db.AccountsDBHelper;
import com.zsh.app.accountscollect.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshihao on 2017/9/20.
 */

public class MainActivity extends BaseActivity {

    private TextView tvAccountsNull;
    private RecyclerView mAccountsRecyclerView;

    private String userName;
    private List<Account> accounts;
    private Cursor cursor;
    private MyAdapter myAdapter;

    public static final int REQCODE_ADD = 0;
    public static final int REQCODE_UPDATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent != null){
            userName = intent.getStringExtra("username");
        }else{
            showShortToast(this,"未接收到用户名，请重新登录！");
            return;
        }
        init();
        if (cursor.getCount() == 0){
            tvAccountsNull.setVisibility(View.VISIBLE);
            mAccountsRecyclerView.setVisibility(View.GONE);
        }else{
            tvAccountsNull.setVisibility(View.GONE);
            mAccountsRecyclerView.setVisibility(View.VISIBLE);
            fillDatas();
            mAccountsRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            mAccountsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAccountsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            myAdapter = new MyAdapter();
            myAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    //跳转到修改记录
                    Intent showIntent = new Intent(MainActivity.this,ShowAccountActivity.class);
                    Account account = accounts.get(position);
                    showIntent.putExtra("appname",account.appName);
                    showIntent.putExtra("account",account.account);
                    showIntent.putExtra("password",account.password);
                    showIntent.putExtra("boundphone",account.boundPhone);
                    showIntent.putExtra("boundemail",account.boundEmail);
                    showIntent.putExtra("username",userName);
                    Cursor idCursor = AccountsDBHelper.getAccountsDBHelper(MainActivity.this)
                            .queryAccount(account);
                    idCursor.moveToFirst();
                    showIntent.putExtra("account_id",idCursor.getInt(0));
                    startActivityForResult(showIntent,REQCODE_UPDATE);
                }

                @Override
                public void onitemLongClick(View itemView, final int position) {
                    AlertDialog.Builder removeDialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("确定删除该账户信息吗？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int result = AccountsDBHelper.getAccountsDBHelper(MainActivity.this)
                                            .deleteAccount(accounts.get(position));
                                    if(result >= 1){
                                        showShortToast(MainActivity.this,"成功删除该条记录");
                                        accounts.remove(position);
                                        myAdapter.notifyDataSetChanged();
                                    }else{
                                        showShortToast(MainActivity.this,"删除记录失败，请稍后再试");
                                    }
                                }
                            })
                            ;
                    removeDialog.show();
                }
            });
            mAccountsRecyclerView.setAdapter(myAdapter);

        }
    }

    private void fillDatas(){
        while(cursor.moveToNext()){
            Account account = new Account();
            account.appName = cursor.getString(cursor.getColumnIndex(AccountsDBHelper.APPNAME));
            account.account = cursor.getString(cursor.getColumnIndex(AccountsDBHelper.ACCOUNT));
            account.password = cursor.getString(cursor.getColumnIndex(AccountsDBHelper.PASSWORD));
            account.boundPhone = cursor.getString(cursor.getColumnIndex(AccountsDBHelper.BOUNDPHONE));
            account.boundEmail = cursor.getString(cursor.getColumnIndex(AccountsDBHelper.BOUNDEMAIL));
            account.userName = userName;
            accounts.add(account);
        }
    }

    private void init(){
        tvAccountsNull = (TextView) findViewById(R.id.tv_accounts_null);
        mAccountsRecyclerView = (RecyclerView) findViewById(R.id.recycler_accounts_show);

        accounts = new ArrayList<>();
        cursor = AccountsDBHelper.getAccountsDBHelper(MainActivity.this).queryAllAccountByUserName(userName);
    }
    //onclick button
    public void addAccount(View view){
        Intent toAdd = new Intent(this,AddAccountActivity.class);
        toAdd.putExtra("username",userName);
        startActivityForResult(toAdd,REQCODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null){
            return;
        }else if (data.getBooleanExtra("isneedfresh",false)){
            accounts.removeAll(accounts);
            cursor = AccountsDBHelper.getAccountsDBHelper(MainActivity.this).queryAllAccount();
            fillDatas();
            if(myAdapter == null){
                tvAccountsNull.setVisibility(View.GONE);
                mAccountsRecyclerView.setVisibility(View.VISIBLE);
                mAccountsRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                mAccountsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAccountsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                myAdapter = new MyAdapter();
                myAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        //跳转到修改记录
                        Intent showIntent = new Intent(MainActivity.this,ShowAccountActivity.class);
                        Account account = accounts.get(position);
                        showIntent.putExtra("appname",account.appName);
                        showIntent.putExtra("account",account.account);
                        showIntent.putExtra("password",account.password);
                        showIntent.putExtra("boundphone",account.boundPhone);
                        showIntent.putExtra("boundemail",account.boundEmail);
                        showIntent.putExtra("username",userName);
                        Cursor idCursor = AccountsDBHelper.getAccountsDBHelper(MainActivity.this)
                                .queryAccount(account);
                        idCursor.moveToFirst();
                        showIntent.putExtra("account_id",idCursor.getInt(0));
                        startActivityForResult(showIntent,REQCODE_UPDATE);
                    }

                    @Override
                    public void onitemLongClick(View itemView, final int position) {
                        AlertDialog.Builder removeDialog = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("确定删除该账户信息吗？")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int result = AccountsDBHelper.getAccountsDBHelper(MainActivity.this)
                                                .deleteAccount(accounts.get(position));
                                        if(result >= 1){
                                            showShortToast(MainActivity.this,"成功删除该条记录");
                                            accounts.remove(position);
                                            myAdapter.notifyDataSetChanged();
                                        }else{
                                            showShortToast(MainActivity.this,"删除记录失败，请稍后再试");
                                        }
                                    }
                                })
                                ;
                        removeDialog.show();
                    }
                });
                mAccountsRecyclerView.setAdapter(myAdapter);
            }else {
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener listener){
            this.onItemClickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MainActivity.this)
            .inflate(R.layout.recycler_item_account,null,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.tvItemAppName.setText(accounts.get(position).appName);
            holder.tvItemAccount.setText(accounts.get(position).account);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onitemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return accounts.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            private TextView tvItemAppName;
            private TextView tvItemAccount;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvItemAppName = (TextView) itemView.findViewById(R.id.tv_item_appname);
                tvItemAccount = (TextView) itemView.findViewById(R.id.tv_item_account);
            }
        }
    }

    private interface OnItemClickListener{
        void onItemClick(View itemView,int position);
        void onitemLongClick(View itemView,int position);
    }

}
