package com.erlite.qrlite.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.erlite.qrlite.MainActivity;
import com.erlite.qrlite.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


public class Encode extends Fragment {

    private EditText url;
    private Button btnGenerate;
    private String encodeUrl = null, encodeUrlStripped;
    public static TextView alert, imageDescriptionText;
    public static ImageView imageOutput;
    private Context context;

    public static boolean alreadyRan = false;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View encView = inflater.inflate(R.layout.fragment_encode, container, false);

        url = encView.findViewById(R.id.linkEncode);
        alert = encView.findViewById(R.id.alertText);
        imageDescriptionText = encView.findViewById(R.id.imageDescriptionText);
        imageOutput = encView.findViewById(R.id.qrImage_gen);
        btnGenerate = encView.findViewById(R.id.generate_btn);
        context = getActivity().getApplicationContext();


           jsonParse();



        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(url.getText().toString())){

                    alert.setText("Nothing To Encode!");
                    alert.setTextColor(getResources().getColor(R.color.alertColor));

                }else {
                    generateQr();
                    alert.setText("Encode Successful!");
                    imageDescriptionText.setText("Generated Image - Tap image to save");
                    alert.setTextColor(getResources().getColor(R.color.successColor));
                    imageOutput.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageDescriptionText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //nothing
                        }
                    });

                    imageOutput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            imageOutput.setDrawingCacheEnabled(true);
                            Bitmap saveBitmap = imageOutput.getDrawingCache();
                            saveImage(saveBitmap);
                        }
                    });
                }
            }
        });

        if (!verifyPermissions()) {
            Toast.makeText(getActivity(), "This app won't work properly without the required permissions!", Toast.LENGTH_SHORT).show();
            return encView;
        }


        return encView;
    }


    //my methods
    private void generateQr() {

        encodeUrl = url.getText().toString().trim();

        //starting qr generation

        //initialize multiformat writer
        MultiFormatWriter writer = new MultiFormatWriter();

        //initialize Bit matrix
        try {
            BitMatrix matrix = writer.encode(encodeUrl, BarcodeFormat.QR_CODE, 300, 300);

            //initialize BarCode Encoder
            BarcodeEncoder encoder = new BarcodeEncoder();

            //initialize bitmap
            Bitmap bitmap = encoder.createBitmap(matrix);

            //set bitmap on imageview
            imageOutput.setImageBitmap(bitmap);

            //initialize input manager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );

            //hide soft keyboard
            manager.hideSoftInputFromWindow(url.getApplicationWindowToken(), 0);

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
    private void saveImage(Bitmap saveBitmap) {


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/link-qr");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 100000;
        n = generator.nextInt(n);
        encodeUrlStripped = encodeUrl.replaceAll("/", "-");
        String fname = "generated-"+ "" + encodeUrlStripped +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            saveBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            Toast.makeText(getActivity().getApplicationContext(), "Generated Image Saved to link-qr directory in storage", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Boolean verifyPermissions() {

        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {

            String[] PERMISSIONS = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 1);
            return false;
        }


        return true;

    }

    private void jsonParse() {

        String feedUrl = "http://www.ericx.tk/json/api/";
        //String feedUrl = "http://erlitefeedqr.epizy.com/json/api/";


        StringRequest stringRequest = new StringRequest(feedUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("feed");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);


                        final String link = object.getString("link");
                        String imageLinkStripped = object.getString("image");
                        String title = object.getString("title");

                        String imageLink = imageLinkStripped.replaceAll("\\/" , "/");


                        Glide.with(context)  //2

                                .load(imageLink) //3
                                .centerCrop() //4
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .fallback(R.drawable.erlitebgimg) //7
                                .into(imageOutput); //8

                        imageOutput.setScaleType(ImageView.ScaleType.CENTER);
                        imageDescriptionText.setText("Sponsored - " + title);




                        imageDescriptionText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                imageDescriptionText.setText("Something went wrong,\nCould not load sponsored post");

            }
        });



        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        if (requestQueue == null){
            requestQueue.add(stringRequest);
        } else{
            //nothing
        }

    }


}
