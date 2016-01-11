package com.rafo.reservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainMenu extends Activity {

    private Korisnik korisnik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        korisnik = null;
        korisnik = (Korisnik) getIntent().getSerializableExtra("korisnik");
        Log.i("RAFOOOOOOO", korisnik.getFirstName() + korisnik.getLastName());
    }

    public void onClickNewReservation(View v){
        Intent intent = new Intent(this, Reserve.class);
        intent.putExtra("userId", korisnik.getId());
        startActivity(intent);
    }
    public void onClickEndReservation(View v){
        Intent intent = new Intent(this, ReturnItem.class);
        intent.putExtra("userId", korisnik.getId());
        startActivity(intent);
    }
    public void onClickInventory(View v){
        //odi direktno na moje rezervacije
        Intent intent = new Intent(this, MyReservations.class);
        intent.putExtra("userId", korisnik.getId());
        startActivity(intent);
    }
    public void onClickProfile(View v){
        Intent intent = new Intent(this, MyProfile.class);
        intent.putExtra("korisnik", korisnik);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //zatvori aplikaciju
        android.os.Process.killProcess(android.os.Process.myPid());
        // This above line close correctly
    }

}
