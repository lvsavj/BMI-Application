package com.example.akshayjadhav.project3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class BmiData extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    /*--------DECLARING-----*/

    TextView tvWelcome,tvLocation;
    SharedPreferences sp1;
    Spinner spnFeet,spnInch;
    EditText etWeight;
    Button btnCalculate,btnHistory;

    GoogleApiClient mLocationClient;
    Location mLastLocation;
    String loc=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_data);


        /*------------BINDING-------------*/

        spnFeet=(Spinner)findViewById(R.id.spnFeet);
        spnInch=(Spinner)findViewById(R.id.spnInch);
        btnCalculate=(Button)findViewById(R.id.btnCalculate);
        btnHistory=(Button)findViewById(R.id.btnHistory);
        etWeight=(EditText)findViewById(R.id.etWeight);
        tvWelcome=(TextView)findViewById(R.id.tvWelcome);
        tvLocation=(TextView)findViewById(R.id.tvLocation);

        //Building Location Client

        mLocationClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        //Setting Welcome Textview

        sp1=getSharedPreferences("UserData",MODE_PRIVATE);
        String name=sp1.getString("name","");
        tvWelcome.setText("Welcome "+name);


        //Building Spinners

        String[] feet={"1","2","3","4","5","6","7","8"};
        ArrayAdapter<String> ad1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,feet);
        spnFeet.setAdapter(ad1);

        String[] inch={"1","2","3","4","5","6","7","8","9","10","11"};
        ArrayAdapter<String> ad2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,inch);
        spnInch.setAdapter(ad2);

        //Calculate BMI Button

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ft = (String) spnFeet.getSelectedItem();
                String in=(String) spnInch.getSelectedItem();
                String wt=etWeight.getText().toString();

                if (wt.length()==0){
                    etWeight.setError("Enter weight");
                    etWeight.requestFocus();
                    return;
                }

                int foot=Integer.parseInt(ft);
                int inches=Integer.parseInt(in);
                double weight=Double.parseDouble(wt);

                while (foot>0){
                    inches=inches+12;
                    foot--;

                }
                double height=inches*2.54;
                double bmi=weight/(height*height);
                bmi=bmi*10000;
                bmi=Math.round(bmi*100)/100.0;

                String msg;
                if (bmi<18.5){
                    msg="You are Underweight";
                }
                else if (bmi>=18.5 & bmi<25){
                    msg="You are Normal";
                }
                else if (bmi>=25 & bmi<30){
                    msg="You are Overweight";
                }
                else {
                    msg="You are Obese";
                }

                Intent i=new Intent(BmiData.this,BmiResult.class);
                i.putExtra("weight",String.valueOf(weight));
                i.putExtra("bmi",String.valueOf(bmi));
                i.putExtra("msg",msg);
                i.putExtra("weight",wt);
                startActivity(i);
            }
        });


        //History button

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(BmiData.this,ViewHistory.class);
                startActivity(i);
            }
        });
    }

    //Overflow Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String myNumber="9870591995";
        String url="www.aksvjd.wordpress.com/contact";
        String email="lv.savj@gmail.com";

        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(getApplicationContext(),"Created by Akshay Jadhav",Toast.LENGTH_LONG).show();
                break;

            case R.id.callus:
                Intent i=new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+myNumber));
                startActivity(i);
                break;

            case R.id.contact:
                Intent c=new Intent(Intent.ACTION_VIEW);
                c.setData(Uri.parse("http:"+url));
                startActivity(c);
                break;

            case R.id.email:
                Intent e=new Intent(Intent.ACTION_SENDTO);
                e.setData(Uri.parse("mailto:"+email));
                startActivity(e);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Location

    @Override
    protected void onStart(){
        super.onStart();
        if(mLocationClient!=null){
            mLocationClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        if (mLastLocation!=null) {
            double latitude=mLastLocation.getLatitude();
            double longitude=mLastLocation.getLongitude();
            Geocoder geocoder=new Geocoder(this, Locale.ENGLISH);

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                if (addresses!=null) {
                    Address fetchedAddress=addresses.get(0);
                    tvLocation.setText(fetchedAddress.getSubLocality());
                    loc=tvLocation.getText().toString();
                } else
                    tvLocation.setText("No address found");

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Could not get address",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        Task1 t1=new Task1();
        t1.execute("http://api.openweathermap.org/data/2.5/weather?units=metric&q="+loc+"&appid="+"3a97c93009f62642a4b3d02bc7415b27");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Connection Suspended",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection Failed",Toast.LENGTH_LONG).show();
    }

    class Task1 extends AsyncTask<String, Void, Double> {
        double temperature;
        @Override
        protected Double doInBackground(String... params) {
            String json="";
            String line;

            try {
                URL url=new URL(params[0]);
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.connect();
                InputStream is=con.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                while ((line=br.readLine())!=null) {
                    json+=line+"\n";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (json!=null) {
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    JSONObject quote=jsonObject.getJSONObject("main");
                    temperature=quote.getDouble("temp");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return temperature;
        }

        @Override
        protected void onPostExecute(Double s) {
            super.onPostExecute(s);
            tvLocation.setText(loc+" | "+s+"°C");
        }
    }


    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Exit");
        alertDialog.show();
    }

}
