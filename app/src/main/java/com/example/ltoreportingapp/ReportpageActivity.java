package com.example.ltoreportingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ReportpageActivity extends AppCompatActivity{
    private static final int REQUEST_CODE_SELECT_VIDEO = 2;
    private static final int PICK_VIDEO = 1;
    private static final int PICK_IMAGE = 2;
    //This are the Variables For Fetching a Multiple Photos
    TextView textView,vidname;
    Button pick;
    Button btn_submit;

    //This are the Variables For Vehicle Plate Number, Color and Type
    EditText editText , editTextremark;
    ImageView Backbtn, img_photo;

    String[] Violation = {"Driving Without A  License", "Not Wearing A Seatbelt", "Reckless Driving", "Driving Under The Influence Of Drugs And Alcohol", "Not Wearing Helmets For Motorcycle Riders" , "Vehicle Registration Violations"
            , "Illegal Transfer Or Use Of Regularly issued Motor Vehicle Plates, Tags or Stickers" , "Driving With Illegal, Damaged or Substandard Parts And Accessories", "Smoke Belching", "Illegal Parking", "Beating The Red Light", "Overtaking",
            "Illegal Turns", "Driving on Prohibited Roads", "No Plate Number", "Dirty Plate Number", "Not Firmly Attached Plate Number", "No Tail Lights", "No Head Light", "No Side Mirror", "Allowing Another Person To Use Drivers License", "Not Wearing SeatBelt",
            "Careless Driving", "Illegal Vehicle Modifications"};

    String[] VColor = {"Black","Blue","Brown","Gold","Gray","Green","Orange","Purple","Red","Silver","Tan","White"
            ,"Yellow","N/A"};

    String[] VType = {"Scooter","Tricycle","E-Tric","Motorcycle","Micro","Sedan","CUV","SUV","HatchBack","RoadSter","Pickup"
            ,"Van","Coupe","SuperCar","Cabriolet","CamperVan","MiniVan","Jeep","E-Jeep","Jeepney","Bus","MiniTruck","Truck","BigTruck","N/A"};

    String Vehicle_Violation, Vehicle_Plate_No, Vehicle_Type, Vehicle_Color, Remark;

    AutoCompleteTextView autoCompleteViolation;
    AutoCompleteTextView autoCompleteVehicleType;
    AutoCompleteTextView autoCompleteVehicleColor;
    ArrayAdapter<String> adapterItemsViolation, adapterItemsVehicleType, adapterItemsVehicleColor;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String encodedImage,encodedVideo ;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportpage);

        textView = findViewById(R.id.totalPhotos);
        vidname = findViewById(R.id.vidname);
        img_photo = findViewById(R.id.img_photo);
        pick = findViewById(R.id.pick);
        btn_submit = findViewById(R.id.btn_submit);


        //This is variable for plate number and backbtn
        editText = findViewById(R.id.plateNumber);
        editTextremark = findViewById(R.id.remark);
        Backbtn = findViewById(R.id.backbtn);

        //This will Upper case all the letter all you input
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        autoCompleteViolation = findViewById(R.id.auto_complete_violation);
        autoCompleteVehicleType = findViewById(R.id.auto_complete_vehicletype);
        autoCompleteVehicleColor = findViewById(R.id.auto_complete_vehiclecolor);

        adapterItemsViolation = new ArrayAdapter<>(this, R.layout.list_item, Violation);
        adapterItemsVehicleType = new ArrayAdapter<>(this, R.layout.list_item, VType);
        adapterItemsVehicleColor = new ArrayAdapter<>(this, R.layout.list_item, VColor);


        autoCompleteViolation.setAdapter(adapterItemsViolation);
        autoCompleteVehicleType.setAdapter(adapterItemsVehicleType);
        autoCompleteVehicleColor.setAdapter(adapterItemsVehicleColor);


        Backbtn.setOnClickListener(view -> {
            Intent intent = new Intent(ReportpageActivity.this, HomepageActivity.class);
            startActivity(intent);
            finish();
        });

        autoCompleteViolation.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(ReportpageActivity.this, "Violation: " + item, Toast.LENGTH_SHORT).show();
        });
        autoCompleteVehicleType.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(ReportpageActivity.this, "Vehicle Type: " + item, Toast.LENGTH_SHORT).show();
        });
        autoCompleteVehicleColor.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(ReportpageActivity.this, "Vehicle Color: " + item, Toast.LENGTH_SHORT).show();
        });
        Button btn_pick_video = findViewById(R.id.UpVid);
        btn_pick_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to pick a video file
                Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                pickVideoIntent.setType("video/*");

                // Start the video picker intent
                startActivityForResult(pickVideoIntent, PICK_VIDEO);
            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to pick an image file
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageIntent.setType("image/*");

                // Start the image picker intent
                startActivityForResult(pickImageIntent, PICK_IMAGE);
            }
        });

        btn_submit.setOnClickListener(view -> {
            Vehicle_Violation = String.valueOf(autoCompleteViolation.getText());
            Vehicle_Plate_No = String.valueOf(editText.getText());
            Vehicle_Type = String.valueOf(autoCompleteVehicleType.getText());
            Vehicle_Color = String.valueOf(autoCompleteVehicleColor.getText());
            Remark = String.valueOf(editTextremark.getText());

            // Store the image
            imageStore(bitmap);

            progressDialog = new ProgressDialog(ReportpageActivity.this);
            progressDialog.setTitle("Info");
            progressDialog.setMessage("Sending Report....");
            progressDialog.show();

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.100.9/Drivewise-Main/DatabaseV2/reportV4.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        progressDialog.dismiss();
                        Toast.makeText(ReportpageActivity.this, response, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ReportpageActivity.this, HomepageActivity.class);
                        startActivity(i);
                        finish();
                    }, error -> {
                progressDialog.dismiss();
                Toast.makeText(ReportpageActivity.this, "Please Fill the required blanks.", Toast.LENGTH_SHORT).show();
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    if (encodedImage != null && !encodedImage.isEmpty()) {
                        params.put("image", encodedImage);
                    } else {
                        params.put("image", "EMPTY");
                    }
                    if (encodedVideo != null && !encodedVideo.isEmpty()) {
                        params.put("video", encodedVideo);
                    } else {
                        params.put("video", "EMPTY");
                    }
                    params.put("Vehicle_Violation",Vehicle_Violation);
                    params.put("Vehicle_Plate_No",Vehicle_Plate_No);
                    params.put("Vehicle_Type",Vehicle_Type);
                    params.put("Vehicle_Color",Vehicle_Color);
                    params.put("Remark",Remark);
                    return params;
                }
            };
            queue.add(stringRequest);
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img_photo.setImageBitmap(bitmap);

                imageStore(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            // Get the name of the selected video file
            String videoName = getFileName(filePath);

            // Display the video name in a TextView or any other appropriate UI element
            vidname.setText(videoName);

            // Call your videoStore() method here to save the video
            videoStore(filePath);
        }
    }


    private void videoStore(Uri videoUri) {
        String videoName = getFileName(videoUri);
        try {
            InputStream inputStream = getContentResolver().openInputStream(videoUri);
            File videoFile = new File(getCacheDir(), videoName);
            OutputStream outputStream = new FileOutputStream(videoFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            // Store the encoded video data
            byte[] videoBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                videoBytes = Files.readAllBytes(videoFile.toPath());
            }
            encodedVideo = Base64.encodeToString(videoBytes, Base64.DEFAULT);

            // Update the video name with the file suffix
            vidname.setText(videoName);

            Toast.makeText(this, "Video file saved to " + videoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void imageStore(Bitmap bitmap) {

        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            encodedImage = "";
        }

    }
}

