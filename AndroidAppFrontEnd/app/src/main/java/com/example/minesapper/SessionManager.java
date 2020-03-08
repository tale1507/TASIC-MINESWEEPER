package com.example.minesapper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRVITE_MODE=0;

    private static final String PREF_NAME="LOGIN";
    private static final String LOGIN="IS_LOGIN";
    private static final String NAME="NAME";
    private static final String EMAIL="EMAIL";
    private static final String IMEPREZIME="IMEPREZIME";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void createSession(String name,String email,String ime_prezime){
        editor.putBoolean(LOGIN,true);
        editor.putString(NAME,name);
        editor.putString(EMAIL,email);
        editor.putString(IMEPREZIME,ime_prezime);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin(){
        if(!this.isLogin()){
            Intent i = new Intent(context,StartApp.class);
            context.startActivity(i);
            ((StartApp) context).finish();
        }
    }
    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user= new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        user.put(IMEPREZIME,sharedPreferences.getString(IMEPREZIME,null));

        return user;
    }
    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context,prijaviSe.class);
        context.startActivity(i);
    }
}
