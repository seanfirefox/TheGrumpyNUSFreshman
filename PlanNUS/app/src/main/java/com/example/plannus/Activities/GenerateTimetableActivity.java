package com.example.plannus.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plannus.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class GenerateTimetableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_timetable);
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url("https://plannus-sat-solver.herokuapp.com/").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("NETWORK_FAIL", "NETWORK FAIL");
                TextView textView = findViewById(R.id.textView);
                textView.setText("Network Fail");
                // Toast.makeText(GenerateTimetableActivity.this, "network not found", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                TextView textView = findViewById(R.id.textView);
                String text = response.body().string();
                Log.d("RESPONSE_BODY", text);
                textView.setText(text);
            }
        });
    }
}