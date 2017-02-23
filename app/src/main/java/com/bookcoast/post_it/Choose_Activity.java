package com.bookcoast.post_it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;

public class Choose_Activity extends AppCompatActivity {


    private Button student_button;
    private Button teacher_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_);

        student_button = (Button) findViewById(R.id.student_button_id);
        teacher_button = (Button) findViewById(R.id.teacher_button_id);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        //student_button.setOnClickListener();

        teacher_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Choose_Activity.this, TeacherLogin.class);

                Choose_Activity.this.startActivity(login);

            }
        });

        student_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Choose_Activity.this, MainActivity.class);

                Choose_Activity.this.startActivity(login);

            }
        });
    }





}
