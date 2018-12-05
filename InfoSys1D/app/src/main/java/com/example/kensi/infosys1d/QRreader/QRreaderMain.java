package com.example.kensi.infosys1d.QRreader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kensi.infosys1d.Checkout.CheckoutMain;
import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.Menu.MenuMain;
import com.example.kensi.infosys1d.Menu.MenuProductAdapter;
import com.example.kensi.infosys1d.Menu.MenuRequest;
import com.example.kensi.infosys1d.PaymentConfirmationMain;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.VolleyCallback;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRreaderMain extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    FrameLayout frameLayout;
    long pastTime = 0;
    long currTime = 0;
    static String savedResult;
    private static String storeID = "";

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public static String getStoreID() {

        return storeID;
    }

    public static void setSavedResult(String setsavedResult) {
        savedResult = setsavedResult;
    }

    public static String getSavedResult() {
        return savedResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreader);
        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
//        textView = (TextView) findViewById(R.id.textView);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.bringToFront();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            //TODO add logout menu for QRreader

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    textView.post(new Runnable() {
                        //upon recieving scanning QR code
                        @Override
                        public void run() {
                            //checks for time, so function doesn't run more than once by accident
                            currTime = System.currentTimeMillis();
                            if (currTime - pastTime > 7000) {
                                pastTime = currTime;
                                //sends request to server for menu
                                MenuRequest.request_call_me(QRreaderMain.this, qrCodes.valueAt(0).displayValue, new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(String result) {
                                        //checks if QR code is valid based on String length sent by server
                                        if (result.length() >= 16) {
                                            setStoreID(qrCodes.valueAt(0).displayValue);
                                            Intent i = new Intent(QRreaderMain.this, MenuMain.class);
                                            i.putExtra("ServerResult", result);
                                            setSavedResult(result);
                                            //vibrates phone
                                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                            vibrator.vibrate(100);
                                            //starts MenuMain
                                            startActivity(i);
                                        } else {
                                            //if invalid QR code
                                            textView.setText(qrCodes.valueAt(0).displayValue);
                                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                            vibrator.vibrate(40);
                                            Toast.makeText(QRreaderMain.this, "Invalid QR Code", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                        }
                    });
                }

            }
        });
    }

    //adds Menu to top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_handicap, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                LoginPostRequest.logout(QRreaderMain.this, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        LoginMain.removeSessionCookie();
                        finish();
                        Intent intent = new Intent(QRreaderMain.this, LoginMain.class);
                        startActivity(intent);
                    }
                });
                return true;
        }
        return false;
    }
}
