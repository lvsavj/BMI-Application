package com.example.akshayjadhav.project3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewHistory extends AppCompatActivity {

    TextView tvHistory;

    final DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        tvHistory=(TextView)findViewById(R.id.tvHistory);

        String data=db.getRecord();
        tvHistory.setText(data);
    }
}
