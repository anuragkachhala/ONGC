package com.tekit.software.ongc.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.tekit.software.ongc.LoginActivity;

public class SessionManager {


    private static final String PREF_NAME = "SharedPref";
    private static final String PREF_REMEMBER = "sharedPrefRemember";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_USER_NAME="userName";
    private static final String KEY_PASSWORD="password";


    private static SessionManager sessionManager;
    private static Context context;
    private SharedPreferences preferences;
    private SharedPreferences sharedPreferences;
    private int status;


    private SessionManager() {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(PREF_REMEMBER, Context.MODE_PRIVATE);

    }




    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        SessionManager.context = context;
    }



    /* Create login session*/











    public static String getUDID() {
        String id = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return id;
    }


    public void createLoginSession(String userName, String password) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }



    private int checkNull(Integer value) {

        return value == null ? 1 : value;
    }















    /* Get stored session data*/











     /* Check login method wil check user login status
    If false it will redirect user to login page
      Else won't do anything*/

  /*  public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, CreateAlertActivity.class);
            context.startActivity(intent);
        }


    }*/


      /*Clear session details
      Clearing all data from Shared Preferences*/


    public void logoutUser(){
        SharedPreferences.Editor editor= preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }




    /*Quick check for login
  Get Login State*/
    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }


}
