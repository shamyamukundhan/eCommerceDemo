package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.paperdb.Paper;

public class LogoutActivity2 extends AppCompatActivity {

    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);


        logout=findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();

                Toast.makeText(LogoutActivity2.this, "You are successfully logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LogoutActivity2.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
}