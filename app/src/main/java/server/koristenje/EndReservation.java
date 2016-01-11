package server.koristenje;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EndReservation extends IntentService {

    StatusLine statusLine;

    public EndReservation() {
        super("EndReservation");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String inventoryNumber = intent.getStringExtra("broj");
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPut put = new HttpPut("http://104.236.71.177/rasus_backend/vracanje");
            put.addHeader(new BasicHeader("content-type", "application/x-www-form-urlencoded"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("broj", inventoryNumber));
            put.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse resp = httpClient.execute(put);
            statusLine = resp.getStatusLine();
            Intent i = new Intent("endReservation");
            sendLocationBroadcast(i);

        }catch (IOException e){

        }

    }

    private void sendLocationBroadcast(Intent intent){
        intent.putExtra("statusCode", statusLine.getStatusCode());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
