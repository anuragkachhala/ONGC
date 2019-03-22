package com.tekit.software.ongc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tekit.software.ongc.Utils.ConnectivityReceiver;
import com.tekit.software.ongc.Utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getName();




    private SessionManager sessionManager;

    @BindView(R.id.et_user_name)
    EditText editTextUserName;

    @BindView(R.id.et_user_password)
    EditText editTextUserPassword;

    @BindView(R.id.btn_sign_in)
    Button buttonSingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ButterKnife.bind(this);

        SessionManager.setContext(getApplicationContext());
        sessionManager = SessionManager.getInstance();

        setOnClickListener();

    }


    public void setOnClickListener() {
        buttonSingIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_sign_in:
                if(checkValidation() && checkConnection() ){

                    sessionManager.createLoginSession(editTextUserName.getText().toString().trim(),editTextUserPassword.getText().toString().trim());
                    startActivity(new Intent(this,HomeActivity.class));
                    finish();

                }
        }
    }


    private boolean checkConnection() {
        if(ConnectivityReceiver.isConnected())
        {
            return true;
        } else {
            Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_LONG).show();
            return false;
         }


    }

    private boolean checkValidation() {
        if (editTextUserName.getText().toString().isEmpty() || !editTextUserName.getText().toString().equals("Admin")) {
            Toast.makeText(this, "Please enter correct user name ", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(editTextUserPassword.getText().toString().isEmpty() || !editTextUserPassword.getText().toString().equals("Admin")){
            Toast.makeText(this, "Please enter correct Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
