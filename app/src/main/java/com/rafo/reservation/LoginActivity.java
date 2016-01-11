package com.rafo.reservation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import server.korisnik.CheckLoginCredentials;

public class LoginActivity extends Activity {

    private TextView textView;
    private FormEditText editTextUserName;
    private FormEditText editTextPassword;
    private TextView registerScreen;
    private ActionProcessButton btnSignIn;
    private List<Korisnik> listaKorisnika = new ArrayList<Korisnik>();
    private static final String TAG_USERNAME = "korisnicko_ime";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "ime";
    private static final String TAG_LASTNAME = "prezime";
    private static final String TAG_PASSWORD = "lozinka";
    private static final String TAG_CREATEDAT = "created_at";
    private static final String TAG_UPDATEDAT = "updated_at";

    private JSONObject users;
    private Korisnik korisnik;
    private String raw_pass;

    FormEditText[] allFields = new FormEditText[2];
    boolean allValid = true;
    boolean postojiKorisnik = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = (TextView) findViewById(R.id.textView);
        editTextUserName = (FormEditText) findViewById(R.id.editTextUserName);
        editTextPassword = (FormEditText) findViewById(R.id.editTextPassword);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);

        allFields[0] =  editTextUserName;
        allFields[1] =  editTextPassword;

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("loginData"));

        registerScreen = (TextView) findViewById(R.id.link_to_register);

        setupListeners();
    }

    private void setupListeners() {
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });

        editTextUserName.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnSignIn.setProgress(0);
                return false;
            }


        });

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnSignIn.setProgress(0);
                return false;
            }


        });
    }

    public void onClickLogin(View view){
        allValid = true;
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        if (allValid) {
            btnSignIn.setProgress(50);
            raw_pass = editTextPassword.getText().toString();
            startService(new Intent(this, CheckLoginCredentials.class));
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonData = intent.getStringExtra("jsonData");
            int responseCode = intent.getIntExtra("responseCode", 400);
            if (responseCode == 200) {
                try {
                    parseData(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                korisnik = checkLoginCredentials();
                if (korisnik == null) {
                    if (!postojiKorisnik)
                        editTextUserName.setError("Ne postoji taj korisnik");
                    else
                        editTextPassword.setError("Pogrešna lozinka");
                    btnSignIn.setProgress(-1);
                } else {
                    Intent i = new Intent(getApplicationContext(), MainMenu.class);
                    btnSignIn.setProgress(100);
                    i.putExtra("korisnik", korisnik);
                    finish();
                    startActivity(i);
                }
            }else{
                Toast.makeText(getApplicationContext(), "Problem pri povezivanju na server", Toast.LENGTH_LONG).show();
                btnSignIn.setProgress(-1);
            }
        }

    };

    private void parseData(String jsonData) throws JSONException {
        JSONArray jsonArr = new JSONArray(jsonData);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject c = jsonArr.getJSONObject(i);
            String userName = c.getString(TAG_USERNAME);
            String password = c.getString(TAG_PASSWORD);
            String name = c.getString(TAG_NAME);
            String lastName = c.getString(TAG_LASTNAME);
            String createdAt = c.getString(TAG_CREATEDAT);
            String updatedAt = c.getString(TAG_UPDATEDAT);
            int id = Integer.valueOf(c.getString(TAG_ID));

            listaKorisnika.add(new Korisnik(createdAt, updatedAt, name, id, lastName, password, userName));
        }
    }

    public Korisnik checkLoginCredentials() {
        String hash = new String(Hex.encodeHex(DigestUtils.sha("2a523e0abba8a8689c71db7db1962b0360a2b49e"
                + raw_pass + "68451251c42ad2ceb558a105025619e8969eb66f")));
        postojiKorisnik = false;
        for (Korisnik korisnik : listaKorisnika){
            if(korisnik.getUserName().equals(editTextUserName.getText().toString())) {
                postojiKorisnik = true;
                if (korisnik.getPassword().equals(hash))
                    return korisnik;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {

        android.os.Process.killProcess(android.os.Process.myPid());
        // This above line close correctly
    }
}