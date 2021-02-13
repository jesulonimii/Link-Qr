package com.erlite.qrlite.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.erlite.qrlite.R;
import com.google.zxing.Result;


public class Scan extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private CodeScanner mCodeScanner;
    private TextView scannedText;
    private ConstraintLayout scanActions;
    private LinearLayout goScannedUrlBtn, copyScanTextBtn;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        scannedText= root.findViewById(R.id.scannedText);
        scanActions =root.findViewById(R.id.scanActions);
        goScannedUrlBtn = root.findViewById(R.id.goScannedUrlbutton);
        copyScanTextBtn = root.findViewById(R.id.scanCopyButton);


       scanner(root);


        return root;

    }

    public void scanner(View root){
        CodeScannerView scannerView = root.findViewById(R.id.scanner);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);


        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
        mCodeScanner.setFlashEnabled(false);
        mCodeScanner.setAutoFocusEnabled(true);



        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result scanResult) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       scannedText.setText(scanResult.toString());
                       scannedText.setTextColor(getResources().getColor(R.color.successColor));
                       scanActions.setVisibility(View.VISIBLE);
                       final String scannedLink = scanResult.toString();




                        goScannedUrlBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //check if scanned text is url
                                if (scannedLink.contains("http://")  || scannedLink.contains("https://")){

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scannedLink));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else{
                                    Toast.makeText(getActivity(), "Not a Url", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });


                        copyScanTextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().getApplicationContext().CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("scanned", scannedText.getText());
                                clipboard.setPrimaryClip(clip);

                                Toast toast = Toast.makeText(getActivity(), "Copied to  clipboard", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}

