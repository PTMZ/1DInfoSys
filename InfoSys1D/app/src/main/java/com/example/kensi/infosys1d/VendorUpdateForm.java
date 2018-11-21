package com.example.kensi.infosys1d;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VendorUpdateForm extends AppCompatActivity {

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
        editTextItemName.setText(getIntent().getStringExtra("itemName"));
        editTextItemName.setEnabled(false);

        editTextCategory = findViewById(R.id.editTextCategory);
        editTextCategory.setText(getIntent().getStringExtra("category"));

        editTextPrice = findViewById(R.id.editTextPrice);
        editTextPrice.setText(getIntent().getStringExtra("price"));

        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDescription.setText(getIntent().getStringExtra("description"));

        buttonAddFinal = findViewById(R.id.buttonAddFinal);
        buttonAddFinal.setText(R.string.update);

        buttonAddFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VendorUpdateForm.this, editTextItemName.getText().toString(), Toast.LENGTH_LONG).show();
                VendorRequests.updateProduct(getApplicationContext(),
                        editTextItemName.getText().toString(),
                        editTextDescription.getText().toString(),
                        editTextCategory.getText().toString(),
                        editTextPrice.getText().toString(),
                        new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Update Product Result: " + result );
                                finish();
                            }
                        }
                );
            }
        });

    }
}
