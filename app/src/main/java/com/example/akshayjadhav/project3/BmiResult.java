package com.example.akshayjadhav.project3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BmiResult extends AppCompatActivity {

    TextView tvResult,tvUn,tvNo,tvOv,tvOb;
    Button btnShare,btnBack,btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_result);

        tvResult=(TextView)findViewById(R.id.tvResult);
        tvUn=(TextView)findViewById(R.id.tvUn);
        tvNo=(TextView)findViewById(R.id.tvNo);
        tvOv=(TextView)findViewById(R.id.tvOv);
        tvOb=(TextView)findViewById(R.id.tvOb);
        btnShare=(Button)findViewById(R.id.btnShare);
        btnBack=(Button)findViewById(R.id.btnBack);
        btnSave=(Button)findViewById(R.id.btnSave);

        final DatabaseHandler db=new DatabaseHandler(this);

        Intent i=getIntent();
        final String weight=i.getStringExtra("weight");
        final String bmi=i.getStringExtra("bmi");
        final String msg=i.getStringExtra("msg");
        String txt="Your BMI is "+bmi+" and "+msg;
        tvResult.setText(txt);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"));
        Date currentLocalTime = cal.getTime();
        final DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
        date.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        final String localTime = date.format(currentLocalTime);

        final double bmid=Double.parseDouble(bmi);

        if (bmid<18.5){
            tvUn.setTextColor(Color.parseColor("#ff0000"));
        }
        else if (bmid>=18.5 & bmid<25){
            tvNo.setTextColor(Color.parseColor("#ff0000"));
        }
        else if (bmid>=25 & bmid<30){
            tvOv.setTextColor(Color.parseColor("#ff0000"));
        }
        else {
            tvOb.setTextColor(Color.parseColor("#ff0000"));
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp1=getSharedPreferences("UserData",MODE_PRIVATE);
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String name=sp1.getString("name","");
                String age=sp1.getString("age","");
                String phone=sp1.getString("phone","");
                String gender=sp1.getString("gender","");

                String details="Name: "+name+"\n"+"Age: "+age+"\n"+"Gender: "+gender+"\n"+"Phone: "+phone+"\n"+"BMI: "+bmid+"\n"+msg+"\n";

                i.putExtra(Intent.EXTRA_TEXT,details);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addRecord(localTime, bmi, weight);
            }
        });
    }
}
