package com.mintsnap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class info extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    EditText i_name, i_country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        i_name = (EditText)findViewById(R.id.i_name);
        i_country = (EditText)findViewById(R.id.i_country);



        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String name =  i_name.getText().toString();
               String country =  i_country.getText().toString();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("i_name", name);
                editor.putString("i_country", country);
                editor.commit(); // commit changes
                Intent i = new Intent(info.this, MainActivity.class);
                startActivity(i);

            }
        });
    }
}
