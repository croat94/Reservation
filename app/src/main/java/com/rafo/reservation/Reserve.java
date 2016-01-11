package com.rafo.reservation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import server.ObradiSliku;
import server.koristenje.MakeNewReservation;


public class Reserve extends Activity {

    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private Button btnSubmit;

    private ImageView imageInventoryNumber;

    private TextView textView3;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatterForServer;

    private String startDate = "";
    private String endDate = "";
    private String inventoryNumber = "";

    private ProgressBar progressBar;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

    private int userId;

    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        userId = getIntent().getIntExtra("userId",0);

        dateFormatter = new SimpleDateFormat("dd.MM.yyyy.", Locale.GERMANY);
        dateFormatterForServer = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("pictureDataReserve"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver2, new IntentFilter("newReservation"));

        findViewsById();
        setDateTimeField();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            textView3.setText("");
            Intent intent = new Intent(this, ObradiSliku.class);
            intent.putExtra("filePath", mCurrentPhotoPath);
            intent.putExtra("caller", "reserve");
            startService(intent);
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

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextStartDate.setText(dateFormatter.format(newDate.getTime()));
                startDate = dateFormatterForServer.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextEndDate.setText(dateFormatter.format(newDate.getTime()));
                endDate = dateFormatterForServer.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void findViewsById() {
        editTextStartDate = (EditText) findViewById(R.id.editTextStartDate);
        editTextStartDate.setInputType(InputType.TYPE_NULL);
        editTextStartDate.requestFocus();
        imageInventoryNumber = (ImageView) findViewById(R.id.imageInventoryNumber);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        imageInventoryNumber.setImageResource(R.drawable.kamera);

        editTextEndDate = (EditText) findViewById(R.id.editTextEndDate);
        editTextEndDate.setInputType(InputType.TYPE_NULL);
        textView3 = (TextView) findViewById(R.id.textView3);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    public void onClickStart(View view) {
        fromDatePickerDialog.show();
    }
    public void onClickEnd(View view) {
        toDatePickerDialog.show();
    }
    public void onClickImage(View view) {
        takePicture();
    }
    public void onClickSubmit(View view){
        if (!inventoryNumber.equals("") && !startDate.equals("") && !endDate.equals(""))
            posaljiPodatkeNaServer();
        else
            Toast.makeText(this, "Popunite sve podatke!", Toast.LENGTH_SHORT).show();
    }
    public void posaljiPodatkeNaServer(){
        Intent intent = new Intent(this, MakeNewReservation.class);
        intent.putExtra("broj", inventoryNumber);
        intent.putExtra("vrijemeOd", startDate);
        intent.putExtra("vrijemeDo", endDate);
        intent.putExtra("userId", userId);
        startService(intent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            inventoryNumber = intent.getStringExtra("inventoryNumber").replaceAll("\\s+", "").replaceAll("\\+"," ");
            progressBar.setVisibility(View.GONE);
            textView3.setText("Text: " + inventoryNumber);
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
                Toast.makeText(getApplicationContext(), "Oprema je rezervirana!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Oprema nije rezervirana!", Toast.LENGTH_SHORT).show();
            }
        }

    };
}
