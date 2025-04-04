package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startLessonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLessonButton = findViewById(R.id.startLessonButton);

        // Обработчик для кнопки "Начать"
        startLessonButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Plan.class);
            startActivity(intent);
        });


    }
}
