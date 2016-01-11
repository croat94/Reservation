package com.rafo.reservation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import server.korisnik.CreateNewUser;

import com.andreabaccega.widget.FormEditText;

public class Register extends Activity {

    private ActionProcessButton btnSignIn;
    private FormEditText editTextName;
    private FormEditText editTextLastName;
    private FormEditText editTextUserName;
    private FormEditText editTextPassword;
    FormEditText[] allFields = new FormEditText[4];
    boolean allValid = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_register);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnRegister);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        editTextName = (FormEditText) findViewById(R.id.editTextName);
        editTextLastName = (FormEditText) findViewById(R.id.editTextLastName);
        editTextUserName = (FormEditText) findViewById(R.id.editTextUserName);
        editTextPassword = (FormEditText) findViewById(R.id.editTextPassword);

        allFields[0] =  editTextName;
        allFields[1] =  editTextLastName;
        allFields[2] =  editTextUserName;
        allFields[3] =  editTextPassword;


        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("registration"));

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
    }
    public void onClickRegister(View view){
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        if (allValid) {
            btnSignIn.setProgress(50);
            try {
                Intent i = new Intent(this, CreateNewUser.class);
                i.putExtra("korisnicko_ime", editTextUserName.getText().toString());
                i.putExtra("lozinka", editTextPassword.getText().toString());
                i.putExtra("ime", editTextName.getText().toString());
                i.putExtra("prezime", editTextLastName.getText().toString());
                startService(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // EditText are going to appear with an exclamation mark and an explicative message.
        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int statusCode = intent.getIntExtra("statusCode", 400);
            if (statusCode != 200){
                btnSignIn.setProgress(-1);
            }else{
                btnSignIn.setProgress(100);
                Toast.makeText(getApplicationContext(), "Registracija uspjesno obavljena", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    };
}