package com.gisfy.unauthorizedlayouts.SQLite;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gisfy.unauthorizedlayouts.DashBoard;
import com.gisfy.unauthorizedlayouts.R;
import com.gisfy.unauthorizedlayouts.Util.WorkaroundMapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.watermark.androidwm_light.WatermarkBuilder;
import com.watermark.androidwm_light.bean.WatermarkText;
import com.zolad.videoslimmer.VideoSlimmer;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    final String TAG = "DEBUGGING";
    private GoogleMap mMap;
    Marker mCurrLocationMarker;
    Spinner noofplots,mandalSpinner, villageSpinner;
    TextInputEditText evillage, eDno, eLocality, eStreetName, eDoorno, eExtent, ePlots, eOwner, eFathername, eAddress1, eNotes,sDistrict, sUlb;
    TextInputEditText ePhoneno;
    TextInputLayout village, Dno, Locality, StreetName, Doorno, Extent, Plots, Owner, Fathername, Address1, Notes,Phoneno,ownerTextLayout;
    ImageView uploadimage,uploadimage2,uploadimage3,uploadimage4;
    String path,path2,path3="NotSelected",path4="NotSelected";
    boolean flag = false;
    Location loc;
    Location location;
    String polygonpoints;
    LatLng latLng;
    SupportMapFragment mapFragment;
    double latit;
    double lngit;
    TextInputEditText tvlat;
    TextInputLayout tvlat_layout;
    boolean polyflag=false;
    TextView  draftsman;
    public static SQLiteHelper mSQLiteHelper;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    ScrollView mScrollView;
    int empID;
    String formattedDate,spinner="";
    String uploadId,uploadId2,uploadId3,uploadId4;
    String savedpath,text12;
    String Videopath=null;
    private Polygon polygon = null;
    private List<LatLng> listLatLngs = new ArrayList<>();
    private List<String> listLatLngsapi = new ArrayList<>();
    private List<Marker> listMarkers = new ArrayList<>();
    String videoname;
    VideoView videoView;
    ImageView edit_latlng,done_latlng;
    boolean islatlngEditable=false;
    MarkerOptions manualmarker;
    LinearLayout villageLayout;
    private boolean isUDA=false;
    private int mandalIds[]=null;
    SharedPreferences sh;

    LocationRequest locationRequest;
    FusedLocationProviderClient locationProviderClient;
    LocationCallback locationCallback;
    private Location currentLocation;
    private static final int UPDATE_INTERVAL = 1000*5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIds();
        mSQLiteHelper = new SQLiteHelper(this, "Layouts.sqlite", null, 1);
        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        configureCameraIdle();
        init();
        // requestLocation();
        tvlat.setEnabled(false);


        String grade = sh.getString("grade",null);
        Cursor cursor = mSQLiteHelper.getMandals();
        int count = cursor.getCount();
        String mandals[]=null;
        if(count!=0){
            mandals = new String[count];
            mandalIds = new int[count];
            for(int i=0;i<count;i++){
                cursor.moveToNext();
                mandalIds[i]=cursor.getInt(0);
                mandals[i]=cursor.getString(1);
            }
            ArrayAdapter mandalAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_dropdown_item_1line
                    ,mandals);
            mandalSpinner.setAdapter(mandalAdapter);
        }else{

        }


        isUDA = grade.contains("UDA");

        if(isUDA){
            village.setVisibility(View.GONE);
            villageLayout.setVisibility(View.VISIBLE);
        }

        villageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Please select Mandal first",Toast.LENGTH_SHORT).show();
            }
        });

        mandalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor1 = mSQLiteHelper.getVillages(mandalIds[position]);
                String villages[];
                int count = cursor1.getCount();
                if(count!=0){
                    if(cursor1.moveToNext()){
                        villages = new String[count];
                        for(int i=0;i<count;i++){
                            villages[i]=cursor1.getString(1);
                            cursor1.moveToNext();
                        }

                        ArrayAdapter villagesAdapter = new ArrayAdapter(MainActivity.this,
                                android.R.layout.simple_dropdown_item_1line,villages);
                        villageSpinner.setAdapter(villagesAdapter);
                        villageLayout.setOnClickListener(null);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edit_latlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvlat.setEnabled(true);
                islatlngEditable=true;
                edit_latlng.setVisibility(View.GONE);
                done_latlng.setVisibility(View.VISIBLE);
            }
        });
        done_latlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvlat.setEnabled(false);
                islatlngEditable=false;
                if (!isValidLatLng(tvlat.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Not Valid LatLng", Toast.LENGTH_SHORT).show();
                } else {
                    String[] latlngstr = tvlat.getText().toString().split(",");
                    double lat = Double.parseDouble(latlngstr[0]);
                    double lng = Double.parseDouble(latlngstr[1]);

                    LatLng coordinate = new LatLng(lat, lng);

                    manualmarker=new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title("Manually Marked")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(manualmarker);

                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                            coordinate, 20);
                    mMap.animateCamera(location);


                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            marker.remove();
                            return false;
                        }
                    });
                }
                done_latlng.setVisibility(View.GONE);
                edit_latlng.setVisibility(View.VISIBLE);
            }
        });

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            if (!(sDistrict.getText().length()==0)) {
                if (!(sUlb.getText().length() == 0)) {

                    if(listMarkers.size()>0){
                        if(polyflag){
                                if (validate(eDno, Dno, 50)) {
                                    if (validate(eLocality, Locality, 50)) {
                                        if (validate(eExtent, Extent, 6)) {
                                            if (validate(ePlots, Plots, 6)) {
                                                if (validateAddress(eAddress1, Address1)) {
                                                    if(validate(eFathername,Fathername,50)) {
                                                        if (validate(eOwner, ownerTextLayout, 50)) {
                                                            if (validatePhone(ePhoneno, Phoneno)) {
                                                                if (path == null) {
                                                                    Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                                } else if (path2 == null) {
                                                                    Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                                } else if (path3 == null) {
                                                                    Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                                } else if (path4 == null) {
                                                                    Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                                } else if (Videopath == null) {
                                                                    Toast.makeText(this, "Please Select Video", Toast.LENGTH_SHORT).show();

                                                                    // LatLngAPI();
                                                                } else {
                                                                    LatLngAPI();
                                                                }
                                                            } else {
                                                                Toast.makeText(this, "Please Enter valid mobile number", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else{
                                                            Toast.makeText(this, "Please Enter owner name", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(this, "Please Enter Father name", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(this, "Please Enter valid address", Toast.LENGTH_SHORT).show();

                                                }
                                            } else {
                                                Toast.makeText(this, "Please Enter number of plots", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "Please Enter number Extent Acres", Toast.LENGTH_SHORT).show();

                                        }

                                    } else {
                                        Toast.makeText(this, "Please Enter Locality", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(this, "Please Enter Survey Number", Toast.LENGTH_SHORT).show();

                                }

                        }
                        else{
                            Toast.makeText(this, "Please connect polygon", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{

                            if (validate(eDno, Dno, 50)) {
                                if (validate(eLocality, Locality, 50)) {
                                    if (validate(eExtent, Extent, 6)) {
                                        if (validate(ePlots, Plots, 6)) {
                                            if (validateAddress(eAddress1, Address1)) {
                                                if(validate(eFathername,Fathername,50)) {
                                                    if (validate(eOwner, ownerTextLayout, 50)) {
                                                        if (validatePhone(ePhoneno, Phoneno)) {
                                                            if (path == null) {
                                                                Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                            } else if (path2 == null) {
                                                                Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                            } else if (path3 == null) {
                                                                Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                            } else if (path4 == null) {
                                                                Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
                                                            } else if (Videopath == null) {
                                                                Toast.makeText(this, "Please Select Video", Toast.LENGTH_SHORT).show();

                                                                // LatLngAPI();
                                                            } else {
                                                                LatLngAPI();
                                                            }
                                                        } else {
                                                            Toast.makeText(this, "Please Enter valid mobile number", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(this, "Please Enter owner name", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(this, "Please Enter Father name", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(this, "Please Enter valid address", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(this, "Please Enter number of plots", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "Please Enter number Extent Acres", Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Toast.makeText(this, "Please Enter Locality", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(this, "Please Enter Survey Number", Toast.LENGTH_SHORT).show();

                            }


                    }


                }
            }
            else
            {
                Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void init() {
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        ArrayAdapter<String> noofplota = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_layout,  getApplicationContext().getResources().getStringArray(R.array.Plots));
        noofplots.setAdapter(noofplota);
        noofplots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    spinner=noofplots.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        videoname = UUID.randomUUID().toString();
        uploadId = UUID.randomUUID().toString();
        uploadId2 = UUID.randomUUID().toString();
        uploadId3 = UUID.randomUUID().toString();
        uploadId4 = UUID.randomUUID().toString();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        String empName = sh.getString("employeename", "");
        draftsman.setText(empName);
        empID=sh.getInt("employeeid",0);
        draftsman.setText(empName);
        sUlb.setText(sh.getString("name",""));
        sDistrict.setText(sh.getString("district",""));


        mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        {
            locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(UPDATE_INTERVAL);
        }

//        client = LocationServices.getFusedLocationProviderClient(this);
        draftsman.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("DEBUGGING","NotEnabled");
            buildAlertMessageNoGps();
        }
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if(locationAvailability.isLocationAvailable()){
                    Log.i(TAG,"Location is available");
                    locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null) {
                                currentLocation = location;
                                latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                Log.i("DEBUGGING", currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 20));
                            }
                        }
                    });

                }else {
                    Log.i(TAG,"Location is unavailable");
                }
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if(currentLocation!=null) {
                    Log.i(TAG, "Location result is available: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                    LatLng midLatLng = mMap.getCameraPosition().target;
//
                }else{
                    Log.i("DEBUGGING","Loc NULL");
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback, MainActivity.this.getMainLooper());
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null) {
                        currentLocation = location;
                        latLng = new LatLng(location.getLatitude(),location.getLongitude());
                        Log.i("DEBUGGING", currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 20));
                    }
                }
            });

            locationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "Exception while getting the location: " + e.getMessage());

                }
            });

        }

    }

    public void result() {
        if (listLatLngs.size()!=0&&polygon==null){
            Toast.makeText(this, "polygon not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(spinner.equals("")){
            Toast.makeText(this, "Select type of plot", Toast.LENGTH_SHORT).show();
        }
        else {
            if (Videopath==null){
                Toast.makeText(this, "Select video", Toast.LENGTH_SHORT).show();
            }
            else{


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Do you want to save the data?");
                String timeStamp = "current_timestamp";

                String slocation = tvlat.getText().toString();
                String[] latlong = slocation.split(",");
                double longitude = Double.parseDouble(latlong[0]);
                double latitude = Double.parseDouble(latlong[1]);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog updateDialog = new ProgressDialog(MainActivity.this);
                        updateDialog.setMessage("Adding data");
                        updateDialog.setCancelable(false);
                        updateDialog.show();
                        try {

                            if (listLatLngsapi.size() != 0) {
                                text12 = listLatLngsapi.toString().replace("[", "").replace("]", "");
                                polygonpoints = text12 + "," + listLatLngsapi.get(0);
                                Log.i("polygonpoints",text12);

                            } else {
                                polygonpoints = "null";
                            }

                            Log.d("DEBUGGING","Polygon::"+polygonpoints);

                            String vill;
                            if(isUDA){
                                vill = villageSpinner.getSelectedItem().toString();
                            }else
                                vill = evillage.getText().toString();

                            mSQLiteHelper.insertData(
                                    draftsman.getText().toString().replace("['\"]+","").trim(),
                                    sDistrict.getText().toString().replace("['\"]+","").trim(),
                                    sUlb.getText().toString().replace("['\"]+","").trim(),
                                    vill.replace("['\"]+","").trim(),
                                    eDno.getText().toString().replace("['\"]+","").trim(),
                                    eLocality.getText().toString().replace("['\"]+","").trim(),
                                    eStreetName.getText().toString().replace("['\"]+","").trim(),
                                    eDoorno.getText().toString().replace("['\"]+","").trim(),
                                    eExtent.getText().toString().replace("['\"]+","").trim(),
                                    ePlots.getText().toString().replace("['\"]+","").trim(),
                                    eOwner.getText().toString().replace("['\"]+","").trim(),
                                    eFathername.getText().toString().replace("['\"]+","").trim(),
                                    eAddress1.getText().toString().replace("['\"]+","").trim(),
                                    ePhoneno.getText().toString().replace("['\"]+","").trim(),
                                    String.valueOf(latitude),
                                    String.valueOf(longitude),
                                    eNotes.getText().toString().replace("['\"]+","").trim(),
                                    empID,
                                    timeStamp,
                                    path,
                                    uploadId,
                                    path2,
                                    uploadId2,
                                    path3,
                                    uploadId3,
                                    path4,
                                    uploadId4,
                                    noofplots.getSelectedItem().toString(),
                                    noofplots.getSelectedItem().toString(),
                                    polygonpoints,
                                    Videopath,
                                    videoname,
                                    mandalSpinner.getSelectedItem().toString());

                            Log.i("polygonpoints",polygonpoints);
                            updateDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Added Success Fully", Toast.LENGTH_SHORT).show();
                            evillage.setText("");
                            eDno.setText("");
                            eLocality.setText("");
                            eStreetName.setText("");
                            eDoorno.setText("");
                            eExtent.setText("");
                            ePlots.setText("");
                            eOwner.setText("");
                            eFathername.setText("");
                            eAddress1.setText("");
                            ePhoneno.setText("");
                            eNotes.setText("");
                            uploadimage.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            listLatLngsapi.clear();
                            finish();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            updateDialog.dismiss();
                            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar.make(mScrollView, "Please plot the correct polygon", Snackbar.LENGTH_SHORT);
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Please Add the Form",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }


    public boolean validate(TextInputEditText editText,TextInputLayout layout,int count)
    {
        boolean validation = false;
        if(editText.getText().length()<=0)
        {
            layout.setErrorEnabled(true);
            layout.setError("Mandate Field");
            requestFocus(editText);
            return false;
        }
        else if (editText.getText().length()>count)
        {
            Toast.makeText(this, "Input cannot exceed the specified limit ", Toast.LENGTH_SHORT).show();
            layout.setErrorEnabled(true);
            layout.setError("Should be less than "+count);
            requestFocus(editText);
            return false;
        }


        else
        {
            validation=true;
            layout.setErrorEnabled(false);
            layout.setError(null);
            layout.clearFocus();
        }

        return true;

    }
    public static boolean isValidLatLng(String str)
    {
        String regex = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }
    public boolean validateAddress(TextInputEditText editText,TextInputLayout layout)
    {
        boolean validation = false;
        if(editText.getText().length()<=0)
        {
            layout.setErrorEnabled(true);
            layout.setError("Mandate Field");
            requestFocus(editText);

        }
        else if (editText.getText().length()>100)
        {
            //Toast.makeText(this, "Input Should be les than 20 char", Toast.LENGTH_SHORT).show();
            layout.setErrorEnabled(true);
            layout.setError("Address Should be less than 100");
            requestFocus(editText);

        }
        else
        {
            validation=true;
            layout.setErrorEnabled(false);
            layout.setError(null);
        }

        return validation;
    }

    public boolean validatePhone(TextInputEditText editText,TextInputLayout layout)
    {
        boolean validation = false;
        if(editText.getText().length()==0)
        {
            layout.setErrorEnabled(true);
            layout.setError("Mandate Field");
            requestFocus(editText);

        }
        else if (editText.getText().length()!=10)
        {
            //Toast.makeText(this, "Input Should be les than 20 char", Toast.LENGTH_SHORT).show();
            layout.setErrorEnabled(true);
            layout.setError("Phone No Should be 10 digits");

            requestFocus(editText);

        }
        else
        {
            validation=true;
            layout.setError(null);
            layout.setErrorEnabled(false);
        }

        return validation;
    }
    private void findViewByIds() {
        edit_latlng=findViewById(R.id.edit_latlng);
        done_latlng=findViewById(R.id.done_latlng);
        tvlat_layout=findViewById(R.id.loc_lat_layout);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        videoView=findViewById(R.id.video_view);
        tvlat = findViewById(R.id.loc_lat);
        noofplots=findViewById(R.id.noofplots);
        draftsman = findViewById(R.id.draftsman);
        sDistrict = findViewById(R.id.district);
        sUlb = findViewById(R.id.ulb);
        uploadimage = findViewById(R.id.profile_image);
        uploadimage2 = findViewById(R.id.profile_image2);
        evillage= findViewById(R.id.village);
        eDno= findViewById(R.id.sno);
        eLocality= findViewById(R.id.locality);
        eStreetName= findViewById(R.id.streetname);
        eDoorno= findViewById(R.id.doorno);
        eExtent= findViewById(R.id.extent);
        ePlots= findViewById(R.id.plots);
        eOwner= findViewById(R.id.owner);
        ownerTextLayout=findViewById(R.id.ownerTextLayout);
        eFathername= findViewById(R.id.fathername);
        eAddress1= findViewById(R.id.address);
        ePhoneno= findViewById(R.id.phoneno);
        eNotes= findViewById(R.id.notes);
//////////////////////////////////////////////////
        village= findViewById(R.id.villageTextLayout);
        Dno= findViewById(R.id.surveyTextLayout);
        Locality= findViewById(R.id.localityTextLayout);
        StreetName= findViewById(R.id.streetnameTextLayout);
        Doorno= findViewById(R.id.doornoTextLayout);
        Extent= findViewById(R.id.extentLayout);
        Plots= findViewById(R.id.plotsTextLayout);
        Fathername= findViewById(R.id.fathernameTextLayout);
        Address1= findViewById(R.id.addressTextLayout);
        Phoneno= findViewById(R.id.phonenumberlayout);
        Notes= findViewById(R.id.noteslayout);

        mandalSpinner = findViewById(R.id.mandalSpinner);
        villageLayout = findViewById(R.id.villageLayout);
        villageSpinner = findViewById(R.id.villageSpinner);

        evillage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        eDno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        eLocality.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        eStreetName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        eDoorno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        eExtent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        ePlots.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        eOwner.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        eFathername.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        eAddress1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        ePhoneno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        eNotes.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        sDistrict.setEnabled(false);
        sUlb.setEnabled(false);
        mScrollView.setFocusableInTouchMode(true);
        mScrollView.setFocusable(true);
    }

    public void setLocation(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
                configureCameraIdle();
        } else
            configureCameraIdle();
    }

//    public void requestLocation() {
//
//        flag = true;
//        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    loc = location;
//                    latit = loc.getLatitude();
//                    lngit = loc.getLongitude();
//                    String ltlg=String.format("%.5f", latit)+","+String.format("%.5f", lngit);
//                    tvlat.setText(ltlg);
//                }
//            }
//        });
//    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                latit= latLng.latitude;
                lngit= latLng.longitude;
                String ltlg=String.format("%.5f", latLng.latitude)+","+String.format("%.5f", latLng.longitude);
                tvlat.setText(ltlg);
            }
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        setGoogleMapClickListener();
        setGoogleMapLongClickListener();
        setPolygonClickListener();

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        }
//        if (googleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this);
//        }


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
//                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
//            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.clear();
        try {
            mMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e)
        {
            startActivity(new Intent(getApplicationContext(), NavigationView.class));
            Toast.makeText(this, "You can't proceed without GPS", Toast.LENGTH_SHORT).show();
        }
    }
//    private void buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//        googleApiClient.connect();
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("DEBUGGING","NotEnabled");
            buildAlertMessageNoGps();
        }
        else
        {
            Log.d("DEBUGGING","Enabled");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }



            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //tvlat.setText(location.getLongitude()+" "+location.getLatitude());

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
            mMap.animateCamera(cameraUpdate);
            configureCameraIdle();
        }
    }
    private void buildAlertMessageNoGps() {
        Log.d("DEBUGGING","SHowing dialog");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You can't proceed without GPS. want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(getApplicationContext(), DashBoard.class));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case 1001:
                    // image 1 -- handle camera image
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        Bitmap wm = Watermark(selectedImage, "lat:" + latit + " lng:" + lngit, " D:" + formattedDate);
                        path = saveReceivedImage(wm, uploadId);
                        Log.i("return", saveReceivedImage(wm, uploadId));
                        uploadimage.setImageBitmap(selectedImage);
                    }else
                        Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
                    break;

                case 1002:
                    // image 1 -- handle galary image
                    if (resultCode == RESULT_OK && data != null){
                        Uri imageURI =  data.getData();
                        Bitmap selectedImage = uriToBitmap(imageURI);
                        Bitmap wm = Watermark(selectedImage, "lat:" + latit + " lng:" + lngit, " D:" + formattedDate);
                        path = saveReceivedImage(wm, uploadId);
                        Log.i("return", saveReceivedImage(selectedImage, uploadId));
                        try {
                            uploadimage.setImageBitmap(selectedImage);
                        }
                        catch (Exception ex){
                            try {
                                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                                int nh = (int) ( bitmap.getHeight() * (375.0 / bitmap.getWidth()) );
                                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                                uploadimage.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        }
                    }else
                        Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
                    break;

                case 2001:
                    // image 2 -- handle camare image
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage2 = (Bitmap) data.getExtras().get("data");
                        Bitmap wm2 = Watermark(selectedImage2, "lat:" + latit + " lng:" + lngit, " D:" + formattedDate);
                        path2 = saveReceivedImage(wm2, uploadId2);
                        uploadimage2.setImageBitmap(selectedImage2);
                        break;
                    }else
                        Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
                    break;

                case 2002:
                    // image 3 -- handle galary image
                    if (resultCode == RESULT_OK && data != null){
                        Uri imageURI =  data.getData();
                        Bitmap selectedImage2 = uriToBitmap(imageURI);
                        Bitmap wm2 = Watermark(selectedImage2, "lat:" + latit + " lng:" + lngit, " D:" + formattedDate);
                        path2 = saveReceivedImage(wm2, uploadId2);

                        try {
                            uploadimage2.setImageBitmap(selectedImage2);
                        }
                        catch (Exception ex){
                            try {
                                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                                int nh = (int) ( bitmap.getHeight() * (375.0 / bitmap.getWidth()) );
                                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                                uploadimage2.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        }

                    }else
                        Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this, "Select Again", Toast.LENGTH_SHORT).show();
        }
        catch (OutOfMemoryError e)
        {
            Toast.makeText(this, "Select Again", Toast.LENGTH_SHORT).show();
        }
        try {
            if (resultCode == this.RESULT_CANCELED) {
                Log.d("what","cancle");
                return;
            }
            if (requestCode == 970) {
                Log.d("what", "gale");
                if (data != null) {
                    Uri contentURI = data.getData();
                    Videopath = getRealPathFromURI(contentURI);
                    Log.d("DEBUGGING VP::",Videopath);
                    if(Videopath.contains("OLDGISFY")){
                        Log.d("DEBUGGING VP","UCIMS PATH");
                        Toast.makeText(MainActivity.this,"Video is corrupted, please select " +
                                        "other video",
                                Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("DEBUGGING VP","NOT UCIMS PATH");
                        PlayVideo(Videopath);
                    }
                }

            } else if (requestCode == 917) {
                Uri contentURI = data.getData();
                Videopath = getRealPathFromURI(contentURI);
                Log.d("DEBUGGING",Videopath);
                PlayVideo( Videopath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Video pick",e.getMessage());
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }
    }
    private Bitmap uriToBitmap(Uri imageUri){
        Bitmap bitmap;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            int nh = (int) ( bitmap.getHeight() * (375.0 / bitmap.getWidth()) );
            bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

        } catch (IOException e) {
            e.printStackTrace();
            bitmap=null;
        }

        Log.i("bitmap", String.valueOf(bitmap));

        return bitmap;
    }
    public String getRealPathFromURI(Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(getFilesDir(), name);
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }


    public void tooglemap(View view) {
        ExpandableLayout expandableLayout=findViewById(R.id.expandable_layout_map);
//        expandableLayout.toggle(true);
        expandableLayout.toggle();
    }
    public void image1(View view) {
        selectImage(1001,1002);
    }

    public void image2(View view) {
        selectImage(2001,2002);
    }
    private void selectImage(final int takePhotoCode, final int pickPhotoCode) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pic photo from");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, takePhotoCode);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , pickPhotoCode);

                }
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to leave this page?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }


    public void connectPolygon(View view) {
        if(listLatLngs.isEmpty())
        {
            polyflag=false;
            Snackbar snackbar= Snackbar.make(mScrollView,"Polygon Should be 3 or more",Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
        else if(listLatLngs.size()<3){
            Snackbar snackbar = Snackbar.make(mScrollView, "Polygon Should be 3 or more", Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
        else {
            polyflag=true;
            if (polygon != null) {
                polygon.remove();
            }
            PolygonOptions polygonOptions = new PolygonOptions().addAll(listLatLngs).clickable(true);
            polygon = mMap.addPolygon(polygonOptions);
        }

    }
    private void setPolygonClickListener(){
        mMap.setOnPolygonClickListener(e -> {
            Snackbar.make(mScrollView, "Polygon clicked!"+polygon, Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setGoogleMapClickListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng e) {
                MarkerOptions markerOptions = new MarkerOptions().position(e);
                Marker marker = mMap.addMarker(markerOptions);
                listMarkers.add(marker);
                listLatLngs.add(e);
                String lats = e.latitude + "--" + e.longitude;
                listLatLngsapi.add(lats);
                Log.i("listLatLngsapi", String.valueOf(listLatLngsapi));
            }
        });
    }

    private void setGoogleMapLongClickListener(){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng e) {
                if (polygon != null) polygon.remove();
                for (Marker marker : listMarkers) marker.remove();
                listMarkers.clear();
                listLatLngsapi.clear();
                polyflag=false;
                listLatLngs.clear();
            }
        });
    }

    public void LatLngAPI() {
        Log.i("lat check",latit+","+lngit);
        String urlString =
                "http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS/WithInUMS?lat="+latit+"&lng="+lngit+
                        "&UMStype=UL";
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait! Checking..");
        pDialog.setCancelable(false);
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String reponse = null;
                        try {
                            Log.i("LatLngAPILog", response.substring(0, 4));
                            reponse = response.substring(0, 4);
                            Log.i("LatLngAPIString", response);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if (reponse.equals("true")) {
                            pDialog.dismiss();
                            result();
                        } else {
                            pDialog.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(mScrollView, "This is nearby Layout information already updated in our system. Do you want to continue to submit?", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Submit", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    result();
                                }
                            });
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Snackbar snackbar = Snackbar
                        .make(mScrollView, "Something went wrong", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        });
        queue.add(stringRequest);
    }

    private String saveReceivedImage(Bitmap bitmap, String imageName){
        try {
            File path = new File(Environment.getExternalStorageDirectory().getPath()+"/UCIMSUCPhotos");
            if (!path.exists()) {
                path.mkdirs();
            }
            File outFile = new File(path, imageName + ".jpg");
            savedpath=path+"/"+imageName+".jpg";
            Log.i(imageName, String.valueOf(path));
            FileOutputStream outputStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("fileout", "Saving received message failed with", e);
        } catch (IOException e) {
            Log.e("fileout", "Saving received message failed with", e);
        }
        return savedpath;
    }

    public Bitmap Watermark(Bitmap bitmap,String latlng_text,String date_text) {
        WatermarkText latlng = new WatermarkText(latlng_text)
                .setPositionX(0)
                .setPositionY(0.2)
                .setTextColor(Color.BLACK)
                .setTextAlpha(150)
                .setRotation(360)
                .setTextSize(15);
        WatermarkText date = new WatermarkText(latlng_text+date_text)
                .setPositionX(0)
                .setPositionY(0.6)
                .setTextColor(Color.BLACK)
                .setTextAlpha(150)
                .setRotation(360)
                .setTextSize(15);
        Bitmap wmimg= WatermarkBuilder
                .create(MainActivity.this, bitmap)
                .loadWatermarkText(latlng)
                .loadWatermarkText(date)// use .loadWatermarkImage(watermarkImage) to load an image.
                .getWatermark()
                .getOutputImage();
        return wmimg;
    }
    public void record(View view) {
        try {
            File path = new File(Environment.getExternalStorageDirectory().getPath() + "UCIMSUCVideos");
            if (!path.exists()) {
                path.mkdirs();
            }
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select video from gallery",
                    "Record video from camera"};
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(galleryIntent, 970);
                                    break;
                                case 1:
                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath() + "UCIMSUCVideos/" + videoname + ".mp4");
                                    startActivityForResult(intent, 917);
                                    break;
                            }
                        }
                    });
            pictureDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PlayVideo(final String input) {

        videoname+="OLDGISFY";

        File myDirectory = new File(Environment.getExternalStorageDirectory(), "UCIMSUCVideos");
        if(!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        if(Build.VERSION.SDK_INT<30){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Compressing Video");
            progressDialog.setCancelable(false);
            try {
                VideoSlimmer.convertVideo(input, Environment.getExternalStorageDirectory() + "/UCIMSUCVideos/" + videoname + ".mp4", 200, 360, 200 * 360 * 30, new VideoSlimmer.ProgressListener() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                    }

                    @Override
                    public void onFinish(boolean result) {
                        progressDialog.dismiss();
                        Log.i("result", String.valueOf(result));
                        if (result) {
                            Videopath = Environment.getExternalStorageDirectory() + "/UCIMSUCVideos/" + videoname + ".mp4";
                        }
                        final MediaController mediacontroller = new MediaController(MainActivity.this);
                        mediacontroller.setAnchorView(videoView);
                        videoView.setMediaController(mediacontroller);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setVideoPath(Videopath);
                        videoView.start();
                    }

                    @Override
                    public void onProgress(float percent) {
                        Log.i("Preogress", String.valueOf(percent));
                        progressDialog.setMessage("Compressing Video "+ Math.floor(percent)+"%");

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            final MediaController mediacontroller = new MediaController(MainActivity.this);
            mediacontroller.setAnchorView(videoView);
            videoView.setMediaController(mediacontroller);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(Videopath);
            videoView.start();
        }

    }

}