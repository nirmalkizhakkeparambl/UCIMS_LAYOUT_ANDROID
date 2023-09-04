package com.gisfy.unauthorizedlayouts;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.gisfy.unauthorizedlayouts.BottomNavigation.NavigationView;
import com.gisfy.unauthorizedlayouts.SQLite.MainActivity;
import com.gisfy.unauthorizedlayouts.SQLite.Model;
import com.gisfy.unauthorizedlayouts.SQLite.RecordListActivity;
import com.gisfy.unauthorizedlayouts.SQLite.RecordListAdapter;
import com.gisfy.unauthorizedlayouts.SQLite.SQLiteHelper;
import com.gisfy.unauthorizedlayouts.Util.WorkaroundMapFragment;
import com.gisfy.unauthorizedlayouts.WMS_Layer.TileProviderFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.watermark.androidwm_light.WatermarkBuilder;
import com.watermark.androidwm_light.bean.WatermarkText;
import com.zolad.videoslimmer.VideoSlimmer;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static com.gisfy.unauthorizedlayouts.SQLite.MainActivity.isValidLatLng;

public class Edit_Sql extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView draftsman;
    TextInputEditText tvlat,eDno,svillage,eLocality,eStreetName,eDoorno,eExtent,ePlots,eOwner,eFathername,eAddress1,ePhoneno,eNotes,sDistrict,sUlb;
    TextInputLayout tvlat_layout,village, Dno, Locality, StreetName, Doorno, Extent, Plots, Owner, Fathername, Address1, Notes,Phoneno;
    String polygonpoints,text12;
    String polygonFromSQL;
    ImageView PreviewImage,PreviewImage2,PreviewImage3,PreviewImage4;
    ExpandableLayout expandableLayout_map;
    Spinner noofplots,mandalSpinner, villageSpinner;
    FusedLocationProviderClient client;
    SQLiteHelper mSQLiteHelper;
    int position;
    ScrollView mScrollView;
    Location location;
    GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Marker mCurrLocationMarker;
    LatLng latLng;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    double latit;
    double lngit;
    ArrayList<Model> mList;

    double latitude;
    double longitude;
    RecordListAdapter mAdapter = null;
    SupportMapFragment mapFragment;
    String picturePath,picturePath2,picturePath3,picturePath4;
    String formattedDate,spinner="",spinner1="";
    String uploadId,uploadId2,uploadId3,uploadId4;
    String savedpath;
    String videoname,videopath;
    VideoView videoView;
    ImageView edit_latlng,done_latlng;
    boolean islatlngEditable=false;
    MarkerOptions manualmarker;
    private Polygon polygon = null;
    boolean polyflag=false;
    private List<LatLng> listLatLngs = new ArrayList<>();
    private List<String> listLatLngsapi = new ArrayList<>();
    private List<Marker> listMarkers = new ArrayList<>();

    LinearLayout villageLayout;
    private boolean isUDA=false;
    private int mandalIds[]=null;
    String mandals[]=null;
    String villageFromSQL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__sql);
        findViewByIds();
        videopath="";
        tvlat.setEnabled(false);

        mSQLiteHelper = new SQLiteHelper(this, "Layouts.sqlite", null, 1);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String grade = sh.getString("grade",null);
        Cursor cursor = mSQLiteHelper.getMandals();
        int count = cursor.getCount();

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
                Toast.makeText(Edit_Sql.this,"Please select Mandal first",Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter villagesAdapter = new ArrayAdapter(Edit_Sql.this, android.R.layout.simple_dropdown_item_1line,villages);
                        villageSpinner.setAdapter(villagesAdapter);
                        villageLayout.setOnClickListener(null);

                        for(int i=0;i<villages.length;i++){
                            if(villages[i].equalsIgnoreCase(villageFromSQL)){
                                villageSpinner.setSelection(i);
                                break;
                            }
                        }

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
                    Toast.makeText(Edit_Sql.this, "Not Valid LatLng", Toast.LENGTH_SHORT).show();
                } else {
                    String[] latlngstr = tvlat.getText().toString().split(",");
                    double lat = Double.parseDouble(latlngstr[0]);
                    double lng = Double.parseDouble(latlngstr[1]);
                    manualmarker=new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title("Manually Marked")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(manualmarker);
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
        //expandableLayout_map.collapse();
        mList = new ArrayList<>();
        uploadId = UUID.randomUUID().toString();
        uploadId2 = UUID.randomUUID().toString();
        uploadId3 = UUID.randomUUID().toString();
        uploadId4 = UUID.randomUUID().toString();
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(cal);
        mAdapter = new RecordListAdapter(this, R.layout.row, mList);
        client= LocationServices.getFusedLocationProviderClient(this);
        ArrayAdapter<String> noofplota = new ArrayAdapter<String>(Edit_Sql.this,
                R.layout.spinner_layout,  getApplicationContext().getResources().getStringArray(R.array.Plots));
        noofplots.setAdapter(noofplota);

        noofplots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    spinner="";
                }
                else{
                    spinner=noofplots.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        position = getIntent().getIntExtra("position",0);
        mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.emap);
        mapFragment.getMapAsync(this);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.emap)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
            configureCameraIdle();
        }
        else
        {
            Toast.makeText(this, "Network is Required to use Map", Toast.LENGTH_SHORT).show();
        }

        mList.clear();

        Cursor c =mSQLiteHelper.getData("SELECT * FROM Layouts WHERE id="+position);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String Draftsman = c.getString(1);
            draftsman.setText(Draftsman);
            String District = c.getString(2);
            sDistrict.setText(District);
            String Ulb = c.getString(3);
            sUlb.setText(Ulb);
            String Village = c.getString(4);
            villageFromSQL = c.getString(4);

            if(!isUDA)
                svillage.setText(Village);

            String Sno = c.getString(5);
            eDno.setText(Sno);
            String Locality = c.getString(6);
            eLocality.setText(Locality);
            String Streetname = c.getString(7);
            eStreetName.setText(Streetname);
            String Dno = c.getString(8);
            eDoorno.setText(Dno);
            String Extent = c.getString(9);
            eExtent.setText(Extent);
            String Plots = c.getString(10);
            ePlots.setText(Plots);
            String Owner = c.getString(11);
            eOwner.setText(Owner);
            String Fathername = c.getString(12);
            eFathername.setText(Fathername);
            String Address1 = c.getString(13);
            eAddress1.setText(Address1);
            polygonFromSQL=c.getString(30);
            String Phoneno = c.getString(14);
            ePhoneno.setText(Phoneno);
            latitude = c.getDouble(15);
            longitude = c.getDouble(16);
            Log.i("editlati", String.valueOf(latitude));

            Log.i("editlati", String.valueOf(longitude));
            tvlat.setText(String.valueOf(longitude)+","+String.valueOf(latitude));
            String Notes  = c.getString(17);
            eNotes.setText(Notes);
            picturePath = c.getString(20);
            picturePath2 = c.getString(22);
            picturePath3 = c.getString(24);
            picturePath4 = c.getString(26);


            String ppoint=c.getString(30);
            videopath=c.getString(31);
            videoname=c.getString(32);

            String m = c.getString(33);
            for(int i=0;i<mandals.length;i++){
                if(m.equalsIgnoreCase(mandals[i])){
                    mandalSpinner.setSelection(i);
                    break;
                }
            }



            Log.i("polygonpoints",ppoint);
            final MediaController mediacontroller = new MediaController(Edit_Sql.this);
            mediacontroller.setAnchorView(videoView);
            videoView.setMediaController(mediacontroller);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(videopath);
            videoView.requestFocus();


            try {
                File f = new File(picturePath);
                Picasso.get().load(f).into(PreviewImage);
                Bitmap selectedImage2 = BitmapFactory.decodeFile(picturePath2);
                PreviewImage2.setImageBitmap(selectedImage2);

                Bitmap selectedImage3 = BitmapFactory.decodeFile(picturePath3);
                PreviewImage3.setImageBitmap(selectedImage3);

                Bitmap selectedImage4 = BitmapFactory.decodeFile(picturePath4);
                PreviewImage4.setImageBitmap(selectedImage4);
            }
            catch (OutOfMemoryError e)
            {
                Toast.makeText(this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            String snoofplots=c.getString(28);
            spinnernoofplots(noofplots,snoofplots);
            if(snoofplots.equals("")){
                spinner="";
            }
            else{
                spinner=snoofplots;
            }
            Log.d("spinner value",spinner);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


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

    private void setGoogleMapClickListener(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng e) {
                MarkerOptions markerOptions = new MarkerOptions().position(e);
                Marker marker = mMap.addMarker(markerOptions);
                listMarkers.add(marker);
                String lats=e.latitude+"--"+e.longitude;
                listLatLngsapi.add(lats);
                listLatLngs.add(e);
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
    public void result() {

        if(spinner.equals("")){
            Toast.makeText(getApplicationContext(),"Select plot type",Toast.LENGTH_SHORT).show();
        }
        else {
            String slocation = tvlat.getText().toString();
            String[] latlong = slocation.split(",");
            String longitude = latlong[0];
            String latitude = latlong[1];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you want to update the data?")
                    .setCancelable(false)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                if (listLatLngsapi.size() != 0) {
                                    text12 = listLatLngsapi.toString().replace("[", "").replace("]", "");
                                    polygonpoints = text12 + "," + listLatLngsapi.get(0);
                                    Log.i("polygonpoints",text12);
                                } else {
                                    polygonpoints = "null";
                                }
                                Log.d("DEBUGGING","PolyUpdateData--"+polygonFromSQL);

                                String vill;
                                if(isUDA){
                                    vill = villageSpinner.getSelectedItem().toString();
                                }else
                                    vill = svillage.getText().toString().trim();

                                mSQLiteHelper.updateData(
                                        draftsman.getText().toString().replace("['\"]+","").trim(),
                                        sDistrict.getText().toString().replace("['\"]+","").trim(),
                                        sUlb.getText().toString().replace("['\"]+","").trim(),
                                        vill.replace("['\"]+",""),
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
                                        latitude,
                                        longitude,
                                        eNotes.getText().toString().replace("['\"]+","").trim(),
                                        picturePath,
                                        picturePath2,
                                        picturePath3,
                                        picturePath4,
                                        noofplots.getSelectedItem().toString(),
                                        videopath,
                                        polygonpoints,
                                        mandalSpinner.getSelectedItem().toString(),
                                        position);
                                Toast.makeText(getApplicationContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Edit_Sql.this,NavigationView.class));
                            } catch (Exception error) {
                                Log.e("Update error", error.getMessage());
                                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("sql er", error.getMessage());
                            }
                            updateRecordList();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
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
    public void eSubmit(View view)
    {
        if (!(sDistrict.getText().length()==0))
        {
            if (!(sUlb.getText().length()==0))
            {

                if(listLatLngsapi.size()>0){
                    if(polyflag){
                        if (validate(svillage,village,20))
                        {
                            if (validate(eDno,Dno,50))
                            {
                                if (validate(eLocality,Locality,50))
                                {
                                    if (validate(eExtent,Extent,6))
                                    {
                                        if (validate(ePlots,Plots,6))
                                        {
                                            if (validateAddress(eAddress1,Address1))
                                            {
                                                if (validatePhone(ePhoneno,Phoneno))
                                                {

                                                    if(validate(eOwner,Owner,50)){
                                                        if(validate(eFathername,Fathername,50)){

                                                            if (videopath.equals("")) {
                                                                Toast.makeText(getApplicationContext(), "Video is mandatory", Toast.LENGTH_SHORT).show();
                                                                Snackbar snackbar = Snackbar.make(mScrollView, "Video is mandatory", Snackbar.LENGTH_SHORT);
                                                            }
                                                            else{
                                                                result();
                                                            }






                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    else{
                        Toast.makeText(this, "Please connect polygon", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    if (validate(svillage,village,20))
                    {
                        if (validate(eDno,Dno,50))
                        {
                            if (validate(eLocality,Locality,50))
                            {
                                if (validate(eExtent,Extent,6))
                                {
                                    if (validate(ePlots,Plots,6))
                                    {
                                        if (validateAddress(eAddress1,Address1))
                                        {
                                            if (validatePhone(ePhoneno,Phoneno))
                                            {

                                                if(validate(eOwner,Owner,50)){
                                                    if(validate(eFathername,Fathername,50)){

                                                        if (videopath.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Video is mandatory", Toast.LENGTH_SHORT).show();
                                                            Snackbar snackbar = Snackbar.make(mScrollView, "Video is mandatory", Snackbar.LENGTH_SHORT);
                                                        }
                                                        else{
                                                            result();
                                                        }






                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
            else
            {
                Toast.makeText(this, "Can't continue without ULB", Toast.LENGTH_SHORT).show();
                sUlb.requestFocusFromTouch();
                sUlb.performClick();
            }
        }
        else
        {
            Toast.makeText(this, "Can't continue without District", Toast.LENGTH_SHORT).show();
            sDistrict.requestFocusFromTouch();
            sDistrict.performClick();
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
            Toast.makeText(this, "Input cannot exceed the specified limit", Toast.LENGTH_SHORT).show();
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

public void spinnernoofplots(Spinner mSpinner,String compareValue)
{
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Plots, (R.layout.spinner_layout));
    adapter.setDropDownViewResource(R.layout.spinner_layout);
    mSpinner.setAdapter(adapter);
    int arg2=0;
    if (compareValue != null) {
        arg2 = adapter.getPosition(compareValue);
        mSpinner.setSelection(arg2);
    }
}

    private void updateRecordList() {
        //get all data from sqlite
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM Layouts");
        mList.clear();
        while (cursor.moveToNext()){

            String Owner = cursor.getString(11);
            String Fathername = cursor.getString(12);
            String Phoneno = cursor.getString(14);
            mList.add(new Model(Owner,Fathername,Phoneno));
        }
    }
    private void configureCameraIdle() {

        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                String ltlg=String.format("%.5f", latLng.latitude)+","+String.format("%.5f", latLng.longitude);
               // tvlat.setText(ltlg);
            }
        };
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if(latitude==0||longitude==0){
            Log.d("DEBUGGING","NUll LATLNG");
        }else{
            Log.d("DEBUGGING","Latitude: "+latitude+" LOngitude: "+longitude);
            LatLng latLng = new LatLng(longitude, latitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

        }

        setGoogleMapClickListener();
        setGoogleMapLongClickListener();
        setPolygonClickListener();
        TileProvider wmsTileProvider = TileProviderFactory.getOsgeoWmsTileProvider();
        googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider));

        if(polygonFromSQL!=null&&!polygonFromSQL.equals("null")){
            Log.d("DEBUGGING","PolyGon::"+polygonFromSQL);

            String points[] = polygonFromSQL.split(",");

            for(String point: points){
                String[] latlng = point.split("--");

                latlng[0]=latlng[0].trim();
                latlng[1]=latlng[1].trim();
                Log.d("DEBUGGING","Lat-"+latlng[0]+"Lng-"+latlng[1]);

                LatLng e = new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]));
                MarkerOptions markerOptions = new MarkerOptions().position(e);
                Marker marker = mMap.addMarker(markerOptions);
                listMarkers.add(marker);
                listLatLngs.add(e);
                String lats = e.latitude + "--" + e.longitude;
                listLatLngsapi.add(lats);
                polyflag=true;
            }

            PolygonOptions polygonOptions = new PolygonOptions().addAll(listLatLngs).clickable(true);
            polygon = mMap.addPolygon(polygonOptions);

        }
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        assert manager != null;
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        else
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please Turn on Gps", Toast.LENGTH_SHORT).show();
        }
            LatLng latLng = new LatLng(longitude, latitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
            //tvlat.setText(Longitude+","+Latitude);
            mMap.animateCamera(cameraUpdate);
        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        startActivity(new Intent(getApplicationContext(),RecordListActivity.class));
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
    private void findViewByIds() {

        mandalSpinner = findViewById(R.id.mandalSpinner);
        villageLayout = findViewById(R.id.villageLayout);
        villageSpinner = findViewById(R.id.villageSpinner);

        edit_latlng=findViewById(R.id.edit_latlng);
        done_latlng=findViewById(R.id.done_latlng);
        tvlat_layout=findViewById(R.id.loc_lat_layout);
        videoView=findViewById(R.id.video_view);
        expandableLayout_map=findViewById(R.id.eexpandable_layout_map);
        PreviewImage=findViewById(R.id.eprofile_image);
        PreviewImage2=findViewById(R.id.eprofile_image2);
        PreviewImage3=findViewById(R.id.eprofile_image3);
        PreviewImage4=findViewById(R.id.eprofile_image4);
        tvlat=findViewById(R.id.loc_lat);
        noofplots=findViewById(R.id.enoofplots);
         draftsman=findViewById(R.id.edraftsman);
         sDistrict=findViewById(R.id.edistrict);
         sDistrict.setEnabled(false);
        svillage=findViewById(R.id.evillage);
         sUlb=findViewById(R.id.eulb);
         sUlb.setEnabled(false);
         eDno=findViewById(R.id.esno);
         eLocality=findViewById(R.id.elocality);
         eStreetName=findViewById(R.id.estreetname);
        eDoorno=findViewById(R.id.edoorno);
         eExtent=findViewById(R.id.eextent);
         ePlots=findViewById(R.id.eplots);
         eOwner=findViewById(R.id.eowner);
         eFathername=findViewById(R.id.efathername);
         eAddress1=findViewById(R.id.eaddress);
         ePhoneno=findViewById(R.id.ephoneno);
        eNotes=findViewById(R.id.enotes);
        mScrollView=findViewById(R.id.escrollview);
        Dno=findViewById(R.id.esurveyTextLayout);
        Locality=findViewById(R.id.elocalityTextLayout);
        StreetName=findViewById(R.id.estreetnameTextLayout);
        Doorno=findViewById(R.id.edoornoTextLayout);
        Extent=findViewById(R.id.eextentLayout);
        Plots=findViewById(R.id.eplotsTextLayout);
        Owner=findViewById(R.id.eownerTextLayout);
        village=findViewById(R.id.evillageTextLayout);
        Fathername=findViewById(R.id.efathernameTextLayout);
        Address1=findViewById(R.id.eaddressTextLayout);
        Phoneno=findViewById(R.id.ephonenumberlayout);
        Notes=findViewById(R.id.enoteslayout);

        svillage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        eDno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {


            switch (requestCode) {

//                case 1213:
//                    String paths = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                    Bitmap selectedImage = BitmapFactory.decodeFile(paths);
//                    Bitmap wm=Watermark(selectedImage,"lat:"+latit+" lng:"+lngit," D:"+formattedDate);
//                    picturePath=saveReceivedImage(wm,uploadId);
//                    Log.i("return",saveReceivedImage(wm,uploadId));
//                    PreviewImage.setImageBitmap(selectedImage);
//                    break;
//                case 2:
//                    String paths2 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                    Bitmap selectedImage2 = BitmapFactory.decodeFile(paths2);
//                    Bitmap wm2=Watermark(selectedImage2,"lat:"+latit+" lng:"+lngit," D:"+formattedDate);
//                    picturePath2=saveReceivedImage(wm2,uploadId2);
//                    PreviewImage2.setImageBitmap(selectedImage2);
//                    break;
//                case 3:
//                    String paths3 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                    Bitmap selectedImage3 = BitmapFactory.decodeFile(paths3);
//                    Bitmap wm3=Watermark(selectedImage3,"lat:"+latit+" lng:"+lngit," D:"+formattedDate);
//                    picturePath3=saveReceivedImage(wm3,uploadId3);
//                    PreviewImage3.setImageBitmap(selectedImage3);
//                    break;
//                case 4:
//                    String paths4 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                    Bitmap selectedImage4 = BitmapFactory.decodeFile(paths4);
//                    Bitmap wm4=Watermark(selectedImage4,"lat:"+latit+" lng:"+lngit," D:"+formattedDate);
//                    picturePath4=saveReceivedImage(wm4,uploadId4);
//                    PreviewImage4.setImageBitmap(selectedImage4);
//                    break;

                case 1001:
                    // image 1 -- handle camera image
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        Bitmap wm = Watermark(selectedImage, "lat:" + latit + " lng:" + lngit, " D:" + formattedDate);
                        picturePath = saveReceivedImage(wm, uploadId);
                        Log.i("return", saveReceivedImage(wm, uploadId));
                        PreviewImage.setImageBitmap(selectedImage);
                    }else
                        Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    // image 1 -- handle galary image
                    if (resultCode == RESULT_OK && data != null){
                        Uri imageURI =  data.getData();
                        Bitmap selectedImage = uriToBitmap(imageURI);
                        Bitmap wm = Watermark(selectedImage, "lat:" + latit + " lng:" + lngit, " D:" + formattedDate);
                        picturePath = saveReceivedImage(wm, uploadId);
                        Log.i("return", saveReceivedImage(wm, uploadId));

                        try {
                            PreviewImage.setImageBitmap(selectedImage);
                        }
                        catch (Exception ex){
                            try {
                                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                                int nh = (int) ( bitmap.getHeight() * (375.0 / bitmap.getWidth()) );
                                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                                PreviewImage.setImageBitmap(bitmap);
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
                        picturePath2 = saveReceivedImage(wm2, uploadId2);
                        PreviewImage2.setImageBitmap(selectedImage2);
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
                        picturePath2 = saveReceivedImage(wm2, uploadId2);

                        try {
                            PreviewImage2.setImageBitmap(selectedImage2);
                        }
                        catch (Exception ex){
                            try {
                                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                                int nh = (int) ( bitmap.getHeight() * (375.0 / bitmap.getWidth()) );
                                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                                PreviewImage2.setImageBitmap(bitmap);
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
            Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show();
        }
        catch (OutOfMemoryError e)
        {
            Toast.makeText(this, "Select Again", Toast.LENGTH_SHORT).show();
        }
        try {
            if (resultCode == this.RESULT_CANCELED) {
                Log.d("what", "cancle");
                return;
            }
            if (requestCode == 970) {
                Log.d("what", "gale");
                if (data != null) {
                    Uri contentURI = data.getData();
                    videopath = getRealPathFromURI(contentURI);
                    Log.d("DEBUGGING VP::",videopath);
                    if(videopath.contains("OLDGISFY")){
                        Log.d("DEBUGGING VP","UCIMS PATH");
                        Toast.makeText(Edit_Sql.this,"Video is corrupted, please select " +
                                        "other video",
                                Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("DEBUGGING VP","NOT UCIMS PATH");
                        PlayVideo(videopath);
                    }
                }
            } else if (requestCode == 917) {
                Uri contentURI = data.getData();
                videopath = getRealPathFromURI(contentURI);
                PlayVideo(videopath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }
    }
    private Bitmap uriToBitmap(Uri imageUri){
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT < 29){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                int nh = (int) ( bitmap.getHeight() * (375.0 / bitmap.getWidth()) );
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            } catch (IOException e) {
                e.printStackTrace();
                bitmap=null;
            }
        }else{
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            } catch (IOException e) {
                bitmap=null;
            }
        }
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


    public void etooglemap(View view)
    {
        //expandableLayout_map.toggle();
    }
    public void eprofile(View view)
    {
        selectImage(1001,1002);
    }
    public void image2(View view) {
        selectImage(2001,2002);
    }

    public void image1(View view) {

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
    public Bitmap Watermark(Bitmap bitmap,String latlng_text,String date_text)
    {
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
                .create(Edit_Sql.this, bitmap)
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
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
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
                            videopath =
                                    Environment.getExternalStorageDirectory() + "/UCIMSUCVideos/" + videoname + ".mp4";
                        }
                        final MediaController mediacontroller = new MediaController(Edit_Sql.this);
                        mediacontroller.setAnchorView(videoView);
                        videoView.setMediaController(mediacontroller);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setVideoPath(videopath);
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
            final MediaController mediacontroller = new MediaController(Edit_Sql.this);
            mediacontroller.setAnchorView(videoView);
            videoView.setMediaController(mediacontroller);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(videopath);
            videoView.start();
        }
    }
    public void cancel(View view) {
       // startActivity(new Intent(this,NavigationView.class));
        finish();
    }
}
