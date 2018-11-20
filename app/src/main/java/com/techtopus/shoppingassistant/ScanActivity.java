package com.techtopus.shoppingassistant;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    static int flag=0;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(ScanActivity.this, new String[] {Manifest.permission.CAMERA}, 0);


        final Dialog mydialogue = new Dialog(ScanActivity.this);

        //startActivity(intent);
        mydialogue.setContentView(R.layout.popupcamera);
        TextView t=(TextView)mydialogue.findViewById(R.id.cl);
        if(flag==0) {
            mydialogue.show();
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialogue.dismiss();


                }
            });
            flag++;
        }

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    public void onBackPressed(){
        Intent i=new Intent(ScanActivity.this,menu.class);
        //i.putExtra("result","8901277007087");
        startActivity(i);
    }
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        String res=rawResult.getText();
        if(res.equals(""))
        {
            onBackPressed();
        }
        Intent i=new Intent(ScanActivity.this,menu.class);
        i.putExtra("result",res);
        //i.putExtra("result","1234566");
        startActivity(i);


        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}