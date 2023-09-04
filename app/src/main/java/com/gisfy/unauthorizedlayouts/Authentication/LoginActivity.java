package com.gisfy.unauthorizedlayouts.Authentication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gisfy.unauthorizedlayouts.BottomNavigation.NavigationView;
import com.gisfy.unauthorizedlayouts.Profile.Splash_Activity;
import com.gisfy.unauthorizedlayouts.R;
import com.gisfy.unauthorizedlayouts.SQLite.SQLiteHelper;
import com.gisfy.unauthorizedlayouts.Util.SessionManagement;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private CardView login_button;
    SessionManagement sessionManagement;
    ImageView img;
    Animation animtop,animbottom;
    CardView cardView;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestPermissions();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging In...");
        progressDialog.setCancelable(false);
        requestQueue = Volley.newRequestQueue(this);

        setType();
        loginOnClick();



        img=findViewById(R.id.imageView);
        cardView=findViewById(R.id.cardView);
        animtop= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fromtop);
        img.setVisibility(View.VISIBLE);
        img.startAnimation(animtop);
        animbottom= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.frombottom);
        cardView.setVisibility(View.VISIBLE);
        cardView.startAnimation(animbottom);
        sessionManagement=new SessionManagement(LoginActivity.this);
        if (sessionManagement.isLoggedIn())
        {
            startActivity(new Intent(LoginActivity.this, NavigationView.class));
        }
    }

    private void setType() {

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login);
    }
    private void loginOnClick() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length() == 0) {
                    email.setError("Be patience and enter Username");
                    email.requestFocus();
                } else if (password.getText().length() == 0) {
                    password.setError("Be patience and enter password");
                    password.requestFocus();
                } else {
                  ConstraintLayout cl = findViewById(R.id.login_layout);
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                            || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                        if (email.getText().length() > 0 && password.getText().length() > 0) {
                            progressDialog.show();
                            final String credentials = "http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS/LoginCheck?userid=" + email.getText().toString() + "&password=" + password.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, credentials,
                                    new Response.Listener<String>() {
                                        String empid;

                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                for (int i = 0; i < 1; i++) {
                                                    org.json.JSONObject jsonobject = jsonArray.getJSONObject(i);
                                                    //progressDialog.dismiss();
                                                    empid = String.valueOf(jsonobject.getInt("employeeid"));
                                                }
                                                if (!(empid == null)) {
                                                    profiledata(empid);

                                                }
                                            } catch (JSONException ex) {
                                                progressDialog.dismiss();
                                                ex.printStackTrace();
                                                Toast.makeText(LoginActivity.this, "Invalid " +
                                                        "details", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "No Network or Maintenance problem", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            //creating a request queue
                            requestQueue.add(stringRequest);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please fill the details", Toast.LENGTH_SHORT).show();
                        }
                    } else if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                            || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
                        Snackbar snackbar = Snackbar
                                .make(cl, "No Network Connection", Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }

                }
            }
        });
    }
    private void profiledata(String empid) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS/ProfileDetails?employeeid="+empid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < 1; i++) {
                                JSONObject empobj = jsonArray.getJSONObject(i);
                                if (empobj==null)
                                {
                                    Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                                }
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("employeeid", empobj.getString("employeeid"));
                                myEdit.putString("employeename", empobj.getString("employeename"));
                                myEdit.putString("designationname", empobj.getString("designationname"));
                                myEdit.putString("emailid", empobj.getString("emailid"));
                                myEdit.putString("ulbname", empobj.getString("ulbname"));
                                myEdit.putString("name", empobj.getString("name"));
                                myEdit.putString("district", empobj.getString("district"));
                                myEdit.putInt("employeeid", empobj.getInt("employeeid"));
                                myEdit.putString("phoneno", empobj.getString("phoneno"));
                                myEdit.putInt("ulbcode", empobj.getInt("ulbcode"));
                                myEdit.putInt("designationid", empobj.getInt("designationid"));
                                myEdit.putString("userid", empobj.getString("userid"));
                                Log.i("userid",empobj.getString("userid"));
                                myEdit.putString("grade",empobj.getString("grade"));
                                myEdit.apply();
                                getMandalsAndVillages(empobj.getString("name"));

                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Something Went wrong",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }


    private void getMandalsAndVillages(String name){
        ConstraintLayout cl = findViewById(R.id.login_layout);

        final String URL ="http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS/Mandal?ULB="+name;
        Log.d("DEBUGGING Mandal url",URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                SQLiteHelper database = new SQLiteHelper(LoginActivity.this, "Layouts.sqlite", null,
                        1);
                database.createtable();
                Log.d("DEBUGGING res",response);
                response=response.trim();
                response=response.replace("\\","");
                response=response.replaceFirst("\"","");
                response = response.substring(0,response.length()-1);
                Log.d("DEBUGGING re",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("DEBUGGING CreateJson",jsonObject.toString());
                    String grade = LoginActivity.this.getSharedPreferences("MySharedPref",
                            MODE_PRIVATE).getString("grade",null);
                    Iterator<String> iterator = jsonObject.keys();

                    while (iterator.hasNext()){
                        String mandal = iterator.next();
                        long rowId = database.insertMandal(mandal);
                        if(grade.contains("UDA")){
                            JSONArray jsonArray = jsonObject.getJSONArray(mandal);
                            for(int villageIndex=0; villageIndex<jsonArray.length(); villageIndex++){
                                database.insertVillage(rowId,jsonArray.getString(villageIndex));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("DEBUGGING CreateJson","Exception");
                }
                progressDialog.dismiss();
                sessionManagement.createLoginSession(email.getText().toString(), password.getText().toString());
                startActivity(new Intent(LoginActivity.this, Splash_Activity.class));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar
                        .make(cl, "Some thing Wrong", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                progressDialog.dismiss();
            }
        });

        requestQueue.add(stringRequest);

    }

    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_NETWORK_STATE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            showSettingsDialog();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();

    }
    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}
