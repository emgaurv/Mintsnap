package com.mintsnap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class feed_upload extends AppCompatActivity {

    EditText input_topic,input_context;
    String filepath;

    String topic,context;
    String formattedDate;
    int internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_upload);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");


        input_topic = (EditText)findViewById(R.id.input_topic);
        input_context = (EditText)findViewById(R.id.input_context);


        formattedDate = df.format(c);




        Bundle extras = getIntent().getExtras();
        filepath = extras.getString("FILE_NAME");
        TextView tv = (TextView)findViewById(R.id.filenameset);

        tv.setText(URLUtil.guessFileName(filepath, null, null));
        Button btnup = (Button) findViewById(R.id.uploaditnow);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic = input_topic.getText().toString();
                context = input_context.getText().toString();

                if(TextUtils.isEmpty(topic) || TextUtils.isEmpty(context) ) {
                    Toast.makeText(getApplication(), "Please Fill Details.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (checkConnectivity() == 1){
                        postFEED();}
                }




            }
        });


    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_mintsnap)
                        .setContentTitle("File Successfully Uploaded!")
                        .setContentText("Visit Feed to view your uploads.");
                         builder.setVibrate(new long[] { 500, 1000 });
                        builder.setDefaults(Notification.DEFAULT_SOUND);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    public void postFEED() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://mintsnap.herokuapp.com/feedpost";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("topic", topic);
            jsonBody.put("context", context);
            jsonBody.put("filepath", filepath);
            jsonBody.put("date", formattedDate);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);

                        addNotification();
                        Intent i = new Intent(feed_upload.this,MainActivity.class);
                        startActivity(i);



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private int checkConnectivity() {
        boolean enabled = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            internet = 0;//sin conexion
            //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("No Internet Connection.");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                 //   Toast.makeText(feed_upload.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });

          /*  alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });*/

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            enabled = false;
        } else {
            internet = 1;//conexión
        }

        return internet;
    }

}
