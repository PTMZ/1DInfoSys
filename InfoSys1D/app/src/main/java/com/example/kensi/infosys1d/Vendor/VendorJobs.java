package com.example.kensi.infosys1d.Vendor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.MyClickListener;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.VolleyCallback;

import java.util.ArrayList;
import java.util.List;

public class VendorJobs extends AppCompatActivity {

    RecyclerView recyclerView;
    VendorJobAdapter adapter;
    List<Job> jobsList;
    TextView textViewTotalTasks;
    private static final String TAG = "VendorJobs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: vendor jobs" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_jobs);

        //creation of a list for each individual item
        jobsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textViewTotalTasks = findViewById(R.id.textViewTotalTasks);

        refreshRecycler();
        /*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshRecycler();
            }
        }, 5000);
        */
        //Volley to server
        //refreshRecycler();

    }

    //adds Menu to top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jobs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            refreshRecycler();
        }
        if(item.getItemId() == R.id.action_logout){
            LoginPostRequest.logout(VendorJobs.this, new VolleyCallback(){
                @Override
                public void onSuccessResponse(String result) {
                    LoginMain.removeSessionCookie();
                    finish();
                }
            });
        }
        return true;
    }

    public void refreshRecycler(){
        VendorRequests.getJobs(VendorJobs.this, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //Updates checkoutProductList with full details from items in checkMap
                //Toast.makeText(getApplicationContext(),  "Refresh", Toast.LENGTH_LONG).show();
                jobsList = VendorRequests.jobs_iterate(result);
                Log.d("JOBS", "Job list size: " + String.valueOf(jobsList.size()));
                //Updates Recycleview
                adapter = new VendorJobAdapter(VendorJobs.this, jobsList, new MyClickListener() {
                    @Override
                    public void onPositionClicked(int position, String type) {
                        int taskId = jobsList.get(position).getTaskId();
                        Log.d("REMOVE_JOB",String.valueOf(taskId));
                        if(type == "REMOVE"){
                            removeItem(taskId);
                        }

                    }
                });
                recyclerView.setAdapter(adapter);
                String temp = "Total tasks: " + String.valueOf(adapter.getItemCount());
                textViewTotalTasks.setText(temp);
            }
        });
    }

    public void removeItem(final int taskId){
        VendorRequests.removeJob(VendorJobs.this, taskId, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Toast.makeText(getApplicationContext(), "Removed: " + String.valueOf(taskId), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "RemoveItem: " + String.valueOf(taskId));
                refreshRecycler();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("BackPress",true);
        setResult(RESULT_OK, i);
        finish();
    }

}


