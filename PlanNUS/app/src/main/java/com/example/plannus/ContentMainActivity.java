package com.example.plannus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ContentMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton checklistImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);

        checklistImgView = findViewById(R.id.checklistImgView);
        checklistImgView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checklistImgView:
                startActivity(new Intent(this, ToDoList.class));
        }
    }

}