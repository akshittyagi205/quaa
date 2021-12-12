package com.quanutrition.app.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class AccountDBUtils {
    SQLiteDatabase db;
    Context context;

    public AccountDBUtils(Context context) {
        this.context = context;
        init();
    }

    void init(){
        db=context.openOrCreateDatabase("AccountDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Accounts(name VARCHAR,id VARCHAR,image VARCHAR,loginStatus VARCHAR);");
    }

    public void insert(String name,String id,String image,String status){

        db.execSQL("INSERT INTO Accounts VALUES('" + name.replaceAll("'","''") + "','" + id +
                "','" + image.replaceAll("'","''") + "','"+status+"');");

    }

    public ArrayList<AccountListModel> get(){
        ArrayList<AccountListModel> dietitianList = new ArrayList<>();
        Cursor c=db.rawQuery("SELECT * FROM Accounts ", null);
        while(c.moveToNext())
        {
            dietitianList.add(new AccountListModel(c.getString(1),c.getString(0),c.getString(2),c.getString(3)));
        }
        Log.d("Account List size",dietitianList.size()+"");
        return dietitianList;
    }

    public void clearData(){

        db.execSQL("DELETE FROM Accounts;");

    }
}
