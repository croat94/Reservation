package server.korisnik;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rafo.reservation.Korisnik;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateUserInfo extends IntentService {

    public UpdateUserInfo() {
        super("UpdateUserInfo");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int userId = intent.getIntExtra("userId", 0);
        String korisnicko_ime = intent.getStringExtra("korisnicko_ime");
        String lozinka = intent.getStringExtra("lozinka");
        String ime = intent.getStringExtra("ime");
        String prezime = intent.getStringExtra("prezime");

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut put = new HttpPut("http://104.236.71.177/rasus_backend/korisnik/" + userId);
            put.addHeader(new BasicHeader("content-type", "application/x-www-form-urlencoded"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("korisnicko_ime", korisnicko_ime));
            nameValuePairs.add(new BasicNameValuePair("lozinka", lozinka));
            nameValuePairs.add(new BasicNameValuePair("ime", ime));
            nameValuePairs.add(new BasicNameValuePair("prezime", prezime));
            put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse resp = httpClient.execute(put);
            String state = String.valueOf(resp.getStatusLine().getStatusCode());
            Log.i("state", "stanje: " + state);
            if (state.equals("200")) {
                Intent i = new Intent("updateInformation");
                sendLocationBroadcast(i);
            }
        }catch (IOException e){

        }

    }


    private void sendLocationBroadcast(Intent intent){
        //intent.putExtra("statusCode", statusLine.getStatusCode());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
