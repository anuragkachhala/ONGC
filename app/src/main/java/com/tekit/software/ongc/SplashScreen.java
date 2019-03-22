package com.tekit.software.ongc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tekit.software.ongc.Model.LocationData;
import com.tekit.software.ongc.Sql.DataBaseAdapter;
import com.tekit.software.ongc.Utils.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public int splash_timeout = 2000;
    private DataBaseAdapter dataBaseAdapter;
    private List<LocationData> locationDataList = new ArrayList<>();

    private SessionManager sessionManager;
    private boolean isLogin = false;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        SessionManager.setContext(getApplicationContext());
        sessionManager = SessionManager.getInstance();
        dataBaseAdapter = new DataBaseAdapter(this);
        isLogin = sessionManager.isLoggedIn();

        locationDataList = dataBaseAdapter.getLocationData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (locationDataList.size() == 0) {
                    readWeatherData();
                    splash_timeout = 1000;
                } else {
                    if(isLogin) {
                        intent= new Intent(SplashScreen.this, HomeActivity.class);

                    }else {
                        intent= new Intent(SplashScreen.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();


                }
            }
        }, splash_timeout);
    }


    private void readWeatherData() {

        List<LocationData> locationDataList = new ArrayList<>();
        // Read the raw csv file
        InputStream is = getResources().openRawResource(R.raw.welllocation);

        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        // Initialization
        String line = "";

        // Initialization
        try {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity", "Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                LocationData locationData = new LocationData();



                Log.e("Size",tokens.length+"");
                // Setters
                /*if(tokens.length>4&& tokens[0]!=null && tokens[1]!=null && tokens[2]!=null && tokens[3]!=null && tokens[6]!=null && tokens[7]!=null) {*/
                if(tokens.length>4) {
                    locationData.setUWI(checkNull(tokens[0]));
                    locationData.setWellName(checkNull(tokens[1]));
                    locationData.setShortName(checkNull(tokens[2]));
                    locationData.setReleaseName(checkNull(tokens[3]));
                    if (tokens[4] != null) {
                        locationData.setLongitude(removeQuotes(tokens[4], "E"));

                    } else {
                        locationData.setLongitude(checkNull(tokens[4]));
                    }
                    if (tokens[5] != null) {
                        locationData.setLat(removeQuotes(tokens[5], "N"));
                    } else {
                        locationData.setLat(checkNull(tokens[5]));
                    }
                    locationData.setWellId(checkNull(tokens[1]));
                    //}
                }


                // Adding object to a class
                locationDataList.add(locationData);



                // Log the object
                Log.d("My Activity", "Just created: " + locationData);
            }

            rowId = dataBaseAdapter.setLocationData(locationDataList);

            if(isLogin) {
                intent= new Intent(SplashScreen.this, HomeActivity.class);

            }else {

                intent= new Intent(SplashScreen.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();


        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }

    private long rowId;


    private String removeQuotes(String latLong,String chart){

        String string=  latLong.replaceAll("^\"|\"$", "").replaceAll("\"","");

        StringBuilder stringBuilder = new StringBuilder(string);
        String a = stringBuilder.insert(stringBuilder.indexOf(chart),"'").insert(stringBuilder.indexOf(chart),"'").toString();
        Log.e("Add",a);
        return  a;


    }

    private String  checkNull(String value){
        if(value == null){
            return  "-1";
        }

        return value;
    }



}

