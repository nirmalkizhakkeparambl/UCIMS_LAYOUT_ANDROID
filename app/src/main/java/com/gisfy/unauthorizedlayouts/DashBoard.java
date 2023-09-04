package com.gisfy.unauthorizedlayouts;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gisfy.unauthorizedlayouts.SQLite.MainActivity;
import com.gisfy.unauthorizedlayouts.SQLite.Model;
import com.gisfy.unauthorizedlayouts.SQLite.RecordListActivity;
import com.gisfy.unauthorizedlayouts.SQLite.SQLiteHelper;
import com.gisfy.unauthorizedlayouts.Sync.SyncRecordList;
import com.gisfy.unauthorizedlayouts.Util.SessionManagement;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;


public class DashBoard extends Fragment {
SQLiteHelper mSQLiteHelper;
    String polygonurl;
    AlertDialog dialog;
    ConstraintLayout cl;
    ProgressDialog pDialog;
    ImageView add;
    ImageView edit;
    ImageView sync;
    String data_response,image_response,poly_response,video_response;
    public DashBoard() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSQLiteHelper = new SQLiteHelper(getContext(), "Layouts.sqlite", null, 1);
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS Layouts(id INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, Draftsman VARCHAR, District VARCHAR, Ulb VARCHAR, Village " +
                "VARCHAR, Sno VARCHAR, Locality VARCHAR, StreetName VARCHAR, DoorNo VARCHAR, " +
                "Extent VARCHAR, Plots VARCHAR, OwnerName VARCHAR, FathersName VARCHAR,Address1 " +
                "VARCHAR, PhoneNo VARCHAR, Latitude VARCHAR, Longitude VARCHAR, Notes VARCHAR, " +
                "Employeeid VARCHAR,timestamp VARCHAR,imagepath VARCHAR,imageuniqueID VARCHAR," +
                "imagepath2 VARCHAR,imageuniqueID2 VARCHAR,imagepath3 VARCHAR,imageuniqueID3 " +
                "VARCHAR,imagepath4 VARCHAR,imageuniqueID4 VARCHAR,noofplots VARCHAR,LatLngs " +
                "VARCHAR,polygon VARCHAR,videopath VARCHAR,videoname VARCHAR,Mandal VARCHAR)");
        View view= inflater.inflate(R.layout.fragment_dash_board, container, false);
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String empName = sh.getString("employeename", "");
        TextView email=view.findViewById(R.id.dash_email);
        if (!(empName==null)) {
            email.setText(empName);
        }
       add =view.findViewById(R.id.add);
       edit =view.findViewById(R.id.edit);
       sync=view.findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("sync","clicked on sync");
                startActivity(new Intent(getContext(), SyncRecordList.class));
            }
        });
        ImageView logout=view.findViewById(R.id.logout);
        cl=view.findViewById(R.id.dashboad_layout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Alert");
                builder.setMessage("Do you Want to Logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        SessionManagement sessionManagement=new SessionManagement(getContext());
                        sessionManagement.logoutUser();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog=builder.create();
                dialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sync","clicked on sync");
                startActivity(new Intent(getContext(), RecordListActivity.class));
            }
        });
//        sync.setOnClickListener(v -> {
//            ConstraintLayout cl=view.findViewById(R.id.dashboad_layout);
//            ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//            if ( connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
//                    || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
//                startActivity(new Intent(DashBoard.this, SyncRecordList.class));
//            }
//            else if ( connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
//                    || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
//                Snackbar snackbar = Snackbar
//                        .make(cl, "No Network Connection", Snackbar.LENGTH_LONG);
//                snackbar.setActionTextColor(Color.RED);
//                snackbar.show();
//            }
//        });


        return view;
    }


}

