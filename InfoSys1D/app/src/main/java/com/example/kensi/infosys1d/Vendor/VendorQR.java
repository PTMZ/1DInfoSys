package com.example.kensi.infosys1d.Vendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.RequestUtils;
import com.example.kensi.infosys1d.VolleyCallback;

public class VendorQR extends AppCompatActivity {

    ImageView qr_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_qr);

        qr_image = findViewById(R.id.qr_image);

        VendorRequests.getQR(VendorQR.this, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                RequestUtils.downloadFile(VendorQR.this, VendorRequests.getQRKey(result), qr_image);
            }
        });
    }

}
