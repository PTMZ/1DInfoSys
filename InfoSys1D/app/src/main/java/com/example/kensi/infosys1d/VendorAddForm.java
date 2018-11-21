package com.example.kensi.infosys1d;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VendorAddForm extends AppCompatActivity {

    EditText editTextItemName;
    EditText editTextCategory;
    EditText editTextPrice;
    EditText editTextDescription;
    Button buttonAddFinal;
    private static final String TAG = "Vendor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: vendor" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        editTextItemName = findViewById(R.id.editTextItemName);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAddFinal = findViewById(R.id.buttonAddFinal);

        buttonAddFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VendorAddForm.this, editTextItemName.getText().toString(), Toast.LENGTH_LONG).show();
                VendorRequests.addProduct(getApplicationContext(),
                        editTextItemName.getText().toString(),
                        editTextDescription.getText().toString(),
                        editTextCategory.getText().toString(),
                        editTextPrice.getText().toString(),
                        new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Add Product Result: " + result );
                                finish();
                            }
                        }
                );
            }
        });

    }
}
