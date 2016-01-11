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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
public class CreateNewUser extends IntentService {

    private StatusLine statusLine;

    public CreateNewUser() {
        super("CreateNewUser");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://104.236.71.177//rasus_backend//korisnik");
        String korisnicko_ime = intent.getStringExtra("korisnicko_ime");
        String lozinka = intent.getStringExtra("lozinka");
        String ime = intent.getStringExtra("ime");
        String prezime = intent.getStringExtra("prezime");
        try {
            // Add your data
            //JSONObject c = jsonArray.getJSONObject(0);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("korisnicko_ime", korisnicko_ime));
            nameValuePairs.add(new BasicNameValuePair("lozinka", lozinka));
            nameValuePairs.add(new BasicNameValuePair("ime", ime));
            nameValuePairs.add(new BasicNameValuePair("prezime", prezime));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            statusLine = response.getStatusLine();
            Log.i("ODGOVOR", "status code: " + statusLine.getStatusCode());
            if (statusLine.getStatusCode() == 200){
                Intent i = new Intent("registration");
                sendLocationBroadcast(i);
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }


    private void sendLocationBroadcast(Intent intent){
        Log.i("AAAAAAAAAAAAAA", "response code: " + statusLine.getStatusCode());
        intent.putExtra("statusCode", statusLine.getStatusCode());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
