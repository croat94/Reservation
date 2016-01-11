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
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import server.korisnik.UpdateUserInfo;

public class MyProfile extends Activity {

    private ActionProcessButton btnSignIn;
    private static Korisnik korisnik;
    private EditText textName;
    private EditText textLastName;
    private EditText textPassword;
    private EditText textUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Korisnik tempKorisnik = (Korisnik) getIntent().getSerializableExtra("korisnik");
        if (korisnik == null || korisnik.getId() != tempKorisnik.getId())
            korisnik = tempKorisnik;
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnUpdateInfo);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("updateInformation"));


        textName = (EditText) findViewById(R.id.textName);
        textName.setText(korisnik.getFirstName());
        textLastName = (EditText) findViewById(R.id.textLastName);
        textLastName.setText(korisnik.getLastName());
        textPassword = (EditText) findViewById(R.id.textPassword);
        textUserName = (EditText) findViewById(R.id.textUserName);
        textUserName.setText(korisnik.getUserName());
    }
    public void onClickUpdateInfo(View view){

        if (!textName.getText().toString().equals("") && !textLastName.getText().toString().equals("")
                && !textUserName.getText().toString().equals("") && !textPassword.getText().toString().equals("")) {
            btnSignIn.setProgress(50);
            textName.setEnabled(false);
            textLastName.setEnabled(false);
            textUserName.setEnabled(false);
            textPassword.setEnabled(false);

            try {
                Intent i = new Intent(this, UpdateUserInfo.class);
                i.putExtra("userId", korisnik.getId());
                i.putExtra("korisnicko_ime", textUserName.getText().toString());
                i.putExtra("lozinka", textPassword.getText().toString());
                i.putExtra("ime", textName.getText().toString());
                i.putExtra("prezime", textLastName.getText().toString());
                startService(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_LONG).show();

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            btnSignIn.setProgress(100);
            Toast.makeText(getApplicationContext(), "Podaci uspjesno azurirani!", Toast.LENGTH_SHORT).show();
            korisnik.setFirstName(textName.getText().toString());
            korisnik.setLastName(textLastName.getText().toString());
            korisnik.setUserName(textUserName.getText().toString());
            korisnik.setPassword(textPassword.getText().toString());
            textName.setEnabled(true);
            textLastName.setEnabled(true);
            textUserName.setEnabled(true);
            textPassword.setEnabled(true);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }

    };
}
