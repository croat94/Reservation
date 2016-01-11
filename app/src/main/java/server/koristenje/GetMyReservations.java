package server.koristenje;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetMyReservations extends IntentService {
    public URL url;
    private StringBuffer text;

    public GetMyReservations() {
        super("GetMyReservations");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        int userId = intent.getIntExtra("userId", 0);
        Log.i("Rafofofofofoof", "userId" + userId);
        try {
            url = new URL("http://104.236.71.177/rasus_backend/korisnik/koristenje/" + userId);
            text = new StringBuffer();
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            InputStreamReader in = new InputStreamReader((InputStream) httpConnection.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line;
            do {
                line = buff.readLine();
                text.append(line + "\n");
            } while (line != null);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Intent i = new Intent("reservationData");
                sendLocationBroadcast(i);
            } else {
                Log.i("RAFOOOOOOOOOOO", "Registration failed for: " + url);
            }
        }catch (IOException e){

        }
    }

    private void sendLocationBroadcast(Intent intent){
        intent.removeExtra("jsonData");
        intent.putExtra("jsonData", text.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
