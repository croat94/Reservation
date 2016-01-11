package server;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ObradiSliku extends IntentService {

    private String inventoryNumber;
    private StatusLine statusLine;
    String responseBody;

    public ObradiSliku() {
        super("ObradiSliku");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String filePath = intent.getStringExtra("filePath");
            String caller = intent.getStringExtra("caller");
            Log.i("SLIKA", filePath);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://104.236.71.177/rasus_ocr/tesseract.php");
            File sourceFile = new File(filePath);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addPart("image", new FileBody(sourceFile));
            httppost.setEntity(entityBuilder.build());

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            responseBody = EntityUtils.toString(response.getEntity());
            statusLine = response.getStatusLine();
            Log.i("ODGOVOR", "response body: -" + responseBody +"-");
            Log.i("ODGOVOR", "status code: " + statusLine.getStatusCode());
            Log.i("ODGOVOR", "reason phrase: " + statusLine.getReasonPhrase());
            Log.i("ODGOVOR", "protocol version: " + statusLine.getProtocolVersion());
            Intent i = null;
            Log.i("CALLER", caller);
            if (caller.equals("reserve")){
                i = new Intent("pictureDataReserve");
            }else if (caller.equals("return")){
                i = new Intent("pictureDataReturn");
            }
            sendLocationBroadcastReturn(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLocationBroadcastReturn(Intent intent){
        intent.putExtra("inventoryNumber", responseBody);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
