package com.rafo.reservation;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import server.ObradiSliku;
import server.koristenje.EndReservation;


public class ReturnItem extends Activity {
    private ImageView imageInventoryNumber;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private ProgressBar progressBar;

    private TextView textViewNumber;

    private String inventoryNumber = "";
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_item);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("pictureDataReturn"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver2, new IntentFilter("endReservation"));

        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        imageInventoryNumber = (ImageView) findViewById(R.id.imageInventoryNumber);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        imageInventoryNumber.setImageResource(R.drawable.kamera);
    }

    private void takePicture() {
        dispatchTakePictureIntent(this);
    }

    private void dispatchTakePictureIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            textViewNumber.setText("");
            Intent intent = new Intent(this, ObradiSliku.class);
            intent.putExtra("filePath", mCurrentPhotoPath);
            intent.putExtra("caller", "return");
            startService(intent);
        }
    }

    public void onClickImage(View view) {
        takePicture();
    }

    public void onClickSubmit(View view){
        if (inventoryNumber.equals(""))
            Toast.makeText(this, "Nije uslikan inventarski broj!", Toast.LENGTH_SHORT).show();
        else
            posaljiPodatkeNaServer();
    }

    public void posaljiPodatkeNaServer(){
        Intent intent = new Intent(this, EndReservation.class);
        intent.putExtra("broj", inventoryNumber);
        startService(intent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            inventoryNumber = intent.getStringExtra("inventoryNumber").replaceAll("\\s+", "").replaceAll("\\+"," ");
            textViewNumber.setText("Text: " + inventoryNumber);
            progressBar.setVisibility(View.GONE);
            Log.i("RAFOOOOOOO", "slika obradena: " + inventoryNumber);
        }

    };

    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int statusCode = intent.getIntExtra("statusCode", 400);
            Log.i("RAFOOOOOOO", "poslano: " + statusCode);
            Log.i("RAFOOOOOOO", "reasonPhrase: " + intent.getStringExtra("reasonPhrase"));
            if (statusCode == 200){
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                Toast.makeText(getApplicationContext(), "Oprema je vracena!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Oprema nije vracena!", Toast.LENGTH_SHORT).show();
            }
        }

    };
}
