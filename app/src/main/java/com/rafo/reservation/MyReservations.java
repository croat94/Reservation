package com.rafo.reservation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import server.koristenje.GetMyReservations;

public class MyReservations extends Activity {

    public List<Rezervacija> listaRezervacija = new ArrayList<>();
    private TextView textViewLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        int userId = getIntent().getIntExtra("userId", 0);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("reservationData"));

        textViewLoading = (TextView) findViewById(R.id.textViewLoading);

        Intent intent = new Intent(this, GetMyReservations.class);
        intent.putExtra("userId", userId);
        startService(intent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonData = intent.getStringExtra("jsonData");
            try {
                parseData(jsonData);
                Rezervacija[] rezervacije = new Rezervacija[listaRezervacija.size()];
                Log.i("RAFOOOOO", "broj rezervacija: " + listaRezervacija.size());
                if (listaRezervacija.size() == 0){
                    textViewLoading.setText("Nema rezervacija");
                }else{
                    textViewLoading.setVisibility(View.INVISIBLE);
                    for (int i = 0; i<listaRezervacija.size(); i++) {
                        rezervacije[i] = listaRezervacija.get(i);
                        Log.i("rezervacija: ", rezervacije[i].getNaziv());
                    }

                    ListAdapter mojAdapter = new CustomAdapter(getApplicationContext(), rezervacije);
                    ListView lista = (ListView) findViewById(R.id.listView);
                    lista.setAdapter(mojAdapter);
                }
            } catch (JSONException e) {

            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }
    };

    private void parseData(String jsonData) throws JSONException {
        JSONArray jsonArr = new JSONArray(jsonData);
        int brojRezerviranihPredmeta = 0;
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject c = jsonArr.getJSONObject(i);
            String date = c.getString("vrijeme");
            int vraceno = c.getInt("vraceno");
            String createdAt = c.getString("created_at");
            //oprema
            JSONObject oprema = c.getJSONObject("oprema");
            String inventar_broj = oprema.getString("inventar_broj");
            String naziv = oprema.getString("naziv");
            if (i == 0 || !listaRezervacija.get(brojRezerviranihPredmeta-1).getCreatedAt().equals(createdAt)){
                listaRezervacija.add(new Rezervacija(naziv, inventar_broj, date, vraceno, createdAt));
                brojRezerviranihPredmeta++;
            }else{
                Rezervacija rezervacija = listaRezervacija.get(brojRezerviranihPredmeta-1);
                rezervacija.setEndDate(date);
            }
        }
    }

    /*@Override
    public void onBackPressed() {
        //zatvori aplikaciju
        android.os.Process.killProcess(android.os.Process.myPid());
        // This above line close correctly
    }
*/
}
