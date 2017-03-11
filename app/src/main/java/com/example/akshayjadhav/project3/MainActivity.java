package com.example.akshayjadhav.project3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    EditText etName,etAge,etPhone;
    RadioGroup rgGender;
    RadioButton rbGender;
    Button btnRegister;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName=(EditText)findViewById(R.id.etName);
        etAge=(EditText)findViewById(R.id.etAge);
        etPhone=(EditText)findViewById(R.id.etPhone);
        rgGender=(RadioGroup)findViewById(R.id.rgGender);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        sp1=getSharedPreferences("UserData",MODE_PRIVATE);

        if (sp1.getBoolean("ne",false)==false) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=etName.getText().toString();
                    String age=etAge.getText().toString();
                    String phone=etPhone.getText().toString();
                    int s1=rgGender.getCheckedRadioButtonId();
                    rbGender=(RadioButton)findViewById(s1);
                    String gender=rbGender.getText().toString();

                    if(name.length()==0) {
                        etName.setError("Enter name");
                        etName.requestFocus();
                        return;
                    }
                    if (age.length()==0) {
                        etAge.setError("Enter age");
                        etAge.requestFocus();
                        return;
                    }
                    if (phone.length()==0) {
                        etPhone.setError("Enter Phone Number");
                        etPhone.requestFocus();
                        return;
                    }
                    if(phone.length()!=10) {
                        etPhone.setError("Invalid Phone Number");
                        etPhone.requestFocus();
                        return;
                    }

                    SharedPreferences.Editor editor=sp1.edit();
                    editor.putString("name",name);
                    editor.putString("age",age);
                    editor.putString("phone",phone);
                    editor.putString("gender",gender);
                    editor.putBoolean("ne",true);
                    editor.commit();

                    Intent i=new Intent(getApplicationContext(),BmiData.class);
                    startActivity(i);
                    finish();

                }
            });
        } else {
            Intent i=new Intent(getApplicationContext(),BmiData.class);
            startActivity(i);
            finish();
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close this application");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.setTitle("Exit");
        alertDialog.show();
    }
}
