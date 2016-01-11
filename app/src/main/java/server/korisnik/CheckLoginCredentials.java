package server.korisnik;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.http.StatusLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class CheckLoginCredentials extends IntentService {

    public URL url;
    private StringBuffer text;
    private static int brojacSlanjaBroadcasta = 0;
    private int responseCode;

    public CheckLoginCredentials() {
        super("CheckLoginCredentials");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        try {
            url = new URL("http://104.236.71.177//rasus_backend//korisnik");
            text = new StringBuffer();
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            responseCode = httpConnection.getResponseCode();
            InputStreamReader in = new InputStreamReader((InputStream) httpConnection.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line;
            do {
                line = buff.readLine();
                text.append(line + "\n");
            } while (line != null);
            Intent i = new Intent("loginData");
            sendLocationBroadcast(i);
        }catch (IOException e){
            Intent i = new Intent("loginData");
            responseCode = 400;
            text.append("");
            sendLocationBroadcast(i);
        }
    }

    private void sendLocationBroadcast(Intent intent){
        intent.putExtra("jsonData", text.toString());
        intent.putExtra("responseCode", responseCode);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
