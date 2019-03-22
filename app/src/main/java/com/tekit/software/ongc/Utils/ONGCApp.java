package com.tekit.software.ongc.Utils;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class ONGCApp extends Application {

    private static ONGCApp  mInstance;


    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Stetho.initializeWithDefaults(this);

        /*sessionManager = new SessionManager(getApplicationContext());

        sessionManager.checkLogin();
*/


    }

    public static ONGCApp getInstance() {
        return mInstance;
    }



    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


}
