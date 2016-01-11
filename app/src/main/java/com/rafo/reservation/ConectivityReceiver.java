package com.rafo.reservation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConectivityReceiver extends BroadcastReceiver {

    private static Toast toast;
    public ConectivityReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected){
            toast = Toast.makeText(context, "No internet connection!", Toast.LENGTH_LONG);
            toast.show();
        }else{
            if (toast != null)
                toast.cancel();
        }
    }
}
