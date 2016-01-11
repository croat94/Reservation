package server.koristenje;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MakeNewReservation extends IntentService {

    private StatusLine statusLine;

    public MakeNewReservation() {
        super("MakeNewReservation");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String vrijemeOd = intent.getStringExtra("vrijemeOd");
        String vrijemeDo = intent.getStringExtra("vrijemeDo");
        String broj = intent.getStringExtra("broj");
        int userId = intent.getIntExtra("userId", 0);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://104.236.71.177//rasus_backend//korisnik/koristenje/"+userId);

        try {
            // Add your data
            //JSONObject c = jsonArray.getJSONObject(0);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("vrijemeOd", vrijemeOd));
            nameValuePairs.add(new BasicNameValuePair("vrijemeDo", vrijemeDo));
            nameValuePairs.add(new BasicNameValuePair("broj", broj));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            statusLine = response.getStatusLine();
            Intent i = new Intent("newReservation");
            sendLocationBroadcast(i);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

    private void sendLocationBroadcast(Intent intent){
        intent.putExtra("statusCode", statusLine.getStatusCode());
        intent.putExtra("reasonPhrase", statusLine.getReasonPhrase());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
