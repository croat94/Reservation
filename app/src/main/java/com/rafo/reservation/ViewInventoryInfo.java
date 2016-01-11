package com.rafo.reservation;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class ViewInventoryInfo extends ActionBarActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory_info);
        userId = getIntent().getIntExtra("userId", 0);
    }

    public void onClickViewPerItem(View view){

    }
    public void onClickMyReservations(View view){
        Intent intent = new Intent(this, MyReservations.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

}
