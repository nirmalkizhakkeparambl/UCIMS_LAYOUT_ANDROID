package com.gisfy.unauthorizedlayouts.Sync;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.gisfy.unauthorizedlayouts.BottomNavigation.NavigationView;
import com.gisfy.unauthorizedlayouts.DashBoard;
import com.gisfy.unauthorizedlayouts.R;
import com.gisfy.unauthorizedlayouts.SQLite.MainActivity;
import com.gisfy.unauthorizedlayouts.SQLite.Model;
import com.gisfy.unauthorizedlayouts.SQLite.SQLiteHelper;
import com.google.android.material.snackbar.Snackbar;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SyncRecordList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<SyncModel> employees = new ArrayList<>();
    private ArrayList<Integer> eids;
    String polygonurl;
    private SyncAdapter adapter;
    private TextView btnGetSelected;
    SQLiteHelper mSQLiteHelper;
    String data_response, image_response;
    LinearLayout relative_snack;
    int xid;
    SimpleDateFormat df;
    String currentDateandTime;
    String empid,userid;
    ProgressDialog pDialog;
    int id;
    String videoresponse="false";
    String Draftsman;
    String District;
    String Ulb;
    String Village;
    String Sno;
    String Locality;
    String Streetname;
    String Dno;
    String Extent;
    String Plots;
    String Owner;
    String Fathername;
    String Address1;
    String Phoneno;
    String Latitude;
    String Longitude;
    String Notes;
    int empid1;
    String timestamp;
    String ImagePath;
    String uploadid;
    String imagepath2;
    String uploadid2;
    String imagepath3;
    String uploadid3;
    String imagepath4;
    String uploadid4;
    String noonfplots;
    String polygonpoints;
    String videofile;
    String videoname;
    String Data_URL   ;
    String Polygon_URL;
    private Async_Thread mTask;
    ProgressDialog pDialog1;
    AlertDialog dialog;

    ConstraintLayout scrollView;
    String poly_response,video_response;
    ArrayList<SyncModel> mList ;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_record_list);
        this.btnGetSelected = (TextView) findViewById(R.id.btnGetSelected);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        relative_snack = (LinearLayout) findViewById(R.id.relative_snack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pDialog=new ProgressDialog(SyncRecordList.this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new SyncAdapter(this, employees);
        recyclerView.setAdapter(adapter);
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentDateandTime = df.format(new Date());
        SharedPreferences sh
                = getSharedPreferences("UserDetails",
                MODE_PRIVATE);
        empid = String.valueOf(sh.getString("EmpId", null));

        SharedPreferences sh1
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        userid = String.valueOf(sh1.getString("userid", null));

        createList();

        if (employees.size() == 0) {
            Snackbar snackbar = Snackbar
                    .make(relative_snack, "No Data Found..?", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Add Now", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(SyncRecordList.this, MainActivity.class));
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }


        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eids = new ArrayList<>();
                eids.clear();
                    if (adapter.getSelected().size() > 0) {
                        for (int i = 0; i < adapter.getSelected().size(); i++) {
                            eids.add(adapter.getSelected().get(i).getId());
                        }
                        Log.i("idsize",String.valueOf(eids.size()));
                        if(eids.size()==1){
                            final ProgressDialog progressDialog1=new ProgressDialog(SyncRecordList.this);
                            progressDialog1.setMessage("Please Wait...");
                            progressDialog1.setCancelable(false);
                            progressDialog1.show();
                            for(int k=0;k<eids.size();k++){
                                Log.i("Syncing id", String.valueOf(eids.get(k)));
                                xid=eids.get(k);
                                mSQLiteHelper = new SQLiteHelper(SyncRecordList.this, "Layouts.sqlite", null, 1);
                                Cursor cursor =mSQLiteHelper.getData("SELECT * FROM Layouts WHERE id="+xid);

                                while (cursor.moveToNext()) {
                                    id                              = cursor.getInt(0);
                                    Draftsman                      = cursor.getString(1);
                                    District                      = cursor.getString(2);
                                    Ulb                            = cursor.getString(3);
                                    Village                      = cursor.getString(4);
                                    Sno                         = cursor.getString(5);
                                    Locality                    = cursor.getString(6);
                                    Streetname                      = cursor.getString(7);
                                    Dno                                 = cursor.getString(8);
                                    Extent                           = cursor.getString(9);
                                    Plots                                 = cursor.getString(10);
                                    Owner                             = cursor.getString(11);
                                    Fathername                         = cursor.getString(12);
                                    Address1                           = cursor.getString(13);
                                    Phoneno                            = cursor.getString(14);
                                    Latitude                          = String.valueOf(cursor.getDouble(15));
                                    Longitude                         = String.valueOf(cursor.getDouble(16));
                                    Notes                              = cursor.getString(17);
                                    empid1                                 = Integer.parseInt(cursor.getString(18));
                                    timestamp                         = cursor.getString(19);
                                    ImagePath                         = cursor.getString(20);
                                    uploadid                          = cursor.getString(21);
                                    imagepath2                        = cursor.getString(22);
                                    uploadid2                              = cursor.getString(23);
                                    imagepath3                       = cursor.getString(24);
                                    uploadid3                        = cursor.getString(25);
                                    imagepath4                           = cursor.getString(26);
                                    uploadid4                        = cursor.getString(27);
                                    noonfplots                    = cursor.getString(28);
                                    polygonpoints               = cursor.getString(30);
                                    polygonpoints=polygonpoints.replaceAll("--"," ");
                                    Log.i("polgygon points",polygonpoints);

                                    videofile                    = cursor.getString(31);
                                    videoname                  = cursor.getString(32);
                                    String mandal = cursor.getString(33);

                                    Log.i("polypoints.lenget", String.valueOf(polygonpoints.length())+polygonpoints);
                                    jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("latitude",Latitude);
                                        jsonObject.put("longitude",Longitude);
                                        jsonObject.put("WPRSBISAD",Draftsman);
                                        jsonObject.put("Name",Owner );
                                        jsonObject.put("FHName",Fathername);
                                        jsonObject.put("Address",Address1);
                                        jsonObject.put("viltown",Village);
                                        jsonObject.put("DoorNo",Dno);
                                        jsonObject.put("locality",Locality);
                                        jsonObject.put("streetname",Streetname);
                                        jsonObject.put("ULB",Ulb);
                                        jsonObject.put("district",District);
                                        jsonObject.put("imagepath",uploadid + ".jpg");
                                        jsonObject.put("Extent",Extent);
                                        jsonObject.put("Plots",Plots);
                                        jsonObject.put("SurveyNo",Sno);
                                        jsonObject.put("PhoneNo",Phoneno);
                                        jsonObject.put("Note",Notes );
                                        jsonObject.put("employeeid",empid1);
                                        jsonObject.put("StartedDate",timestamp);
                                        jsonObject.put("roadType",noonfplots);
                                        jsonObject.put("imagepath1",uploadid2 + ".jpg");
                                        jsonObject.put("imagepath2",uploadid3 + ".jpg");
                                        jsonObject.put("imagepath3",uploadid4 + ".jpg");
                                        jsonObject.put("Mandal",mandal );
                                        jsonObject.put("LayoutVideo",videoname + ".mp4");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                        String Url="http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS/insertLayout";
                                    if (checkPermission()) {
                                        requestPermissionAndContinue();
                                    }else {

                                        Log.i("polgygon points",polygonpoints);
                                        if(polygonpoints.equals("null")){

                                            polygonurl="null";

                                            Log.i("polgygon entered null",polygonurl);
                                        }
                                        else{

                                            polygonurl  = "http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS?query=INSERT INTO public.\"tblULPolygon\"(geom, \"OwnerName\",\"employeeid\",\"Date\") VALUES (\n" +
                                                    "\tST_GeometryFromText('MULTIPOLYGON(((" + polygonpoints + ")))',4326)" +
                                                    ",'" + Owner +
                                                    "','" + userid +"',current_timestamp)";
                                        }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog1.dismiss();
                                                mTask=new Async_Thread();
                                                mTask.execute(videoname,videofile,uploadid,ImagePath,uploadid2,imagepath2,uploadid3, imagepath3,uploadid4, imagepath4,Url,polygonurl,String.valueOf(xid));
                                            }
                                        }, 3000);
                                    }
                                }
                            }
                        }
                        else{
                            showToast("Multiple entries are not allowed");
                        }
                    }
                 else {
                    showToast("select atleast one entry to sync");
                }
            }
        });
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permissions");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SyncRecordList.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, 1);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(SyncRecordList.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, 1);
            }
        } else {

        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public  void Multipart_Image(String image_name,String Path)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("qwerty",image_name,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(Path)))
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS/UploadImage?imgname="+image_name)
                .method("POST", body)
                .build();

        try {
            okhttp3.Response response = client.newCall(request).execute();
            Log.i("Multipart_Image",response.message());
            Log.i("Multipart_Image", String.valueOf(response));
            image_response="OK";
        } catch (Exception exc) {
            Log.i("error", exc.getMessage());
            image_response="Failed";
        }
    }

    private class Async_Thread extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(SyncRecordList.this);
            pDialog1.setMessage("Please wait...Syncing");
            pDialog1.setIndeterminate(false);
            pDialog1.setCancelable(false);
            pDialog1.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String videoname= strings[0];
            String videopath= strings[1];

            String imgname= strings[2];
            String imgpath= strings[3];

            String imgname2= strings[4];
            String imgpath2= strings[5];

            String imgname3= strings[6];
            String imgpath3= strings[7];

            String imgname4= strings[8];
            String imgpath4= strings[9];

            Data_URL    = strings[10];
            Polygon_URL = strings[11];
            String xid= strings[12];


            Log.i("path2",imgpath2);
            Log.i("path3",imgpath3);
            Log.i("path4",imgpath4);
                if(uploadvideo(videoname,videopath).equals("true")){
                    Multipart_Image(imgname,imgpath);
                    Multipart_Image(imgname2,imgpath2);
                    if((imgpath3.equals("NotSelected"))){
                        Log.i("imgpath3","entered3");

                    }
                    else{
                        Multipart_Image(imgname3,imgpath3);
                    }
                    if(imgpath4.equals("NotSelected")){
                        Log.i("imgpath4","entered4");

                    }
                    else{
                        Multipart_Image(imgname4,imgpath4);
                    }

                    return xid;
                }
                else{
                    return null;
                }

        }
        @Override
        protected void onPostExecute(String result) {

            if(result!=null){
                int xid=Integer.parseInt(result);
                if(!(Polygon_URL.equals("null"))){
                    Log.i("Polygon_URL",Polygon_URL);
                    URL_Exec1(Polygon_URL,"Polygon",xid,Data_URL,"Data");
                }
                else{
                    URL_Exec(Data_URL,"Data",xid);
                }
                //progressDialog.dismiss();
            }
            else{

                Log.i("onpostexecute","caught here");
                mTask.cancel(true);
                pDialog1.dismiss();
                Toast.makeText(SyncRecordList.this,"Some error occured\tTry after sometime",Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(result);
        }
    }
    private void deleteentry(int id){
        Toast.makeText(SyncRecordList.this, "Successfully Synced", Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(relative_snack, "Sync Success", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
        Log.i("deleteid", String.valueOf(id));
        mSQLiteHelper.deleteData(id);
        pDialog1.dismiss();
        createList();
    }
    public String uploadvideo(String videoname,String path){
        okhttp3.Response response = null;
        OkHttpClient client=new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES); // read timeout
        client = builder.build();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("qwerty",videoname,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(path)))
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://ucimsapdtcp.ap.gov.in/UMSAPI/api/UMS/UploadVideo?VideoName="+videoname)
                .method("POST", body)
                .build();
        try {
            response = client.newCall(request).execute();
            Log.i("response", String.valueOf(response));
            assert response.body() != null;
            videoresponse = response.body().string();
            Log.i("Video_Response", videoresponse);

        } catch (IOException e) {
            e.printStackTrace();
            response = null;
            video_response = "false";
        }
        return videoresponse;
    }

    public String URL_Exec(String url, final String type, final int xid)
    {
        RequestQueue queue = Volley.newRequestQueue(SyncRecordList.this);
        Log.d("DEBUGGING",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog1.dismiss();
                        Log.i("URL_Exec", response);
                        if (response.equals("\"inserted\"")) {
                            deleteentry(xid);
                            data_response=response;
                        }else{
                            Toast.makeText(SyncRecordList.this,"Something went wrong",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("onpostexecute","caught here");
                mTask.cancel(true);
                pDialog1.dismiss();
                Toast.makeText(SyncRecordList.this,"Some error occured\tTry after sometime",Toast.LENGTH_SHORT).show();

                if (type.equals("Data")) {
                    Log.i("URL_Exec_Data/Polygon", String.valueOf(error));
                }

                Log.i("URL_Exec_Data/Polygon", String.valueOf(error));
                data_response="Failed";
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String requestBody = jsonObject.toString();
                Log.d("REquestBody",requestBody);
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(stringRequest);
        return data_response;
    }

    public String URL_Exec1(String url, final String type, final int xid, final String data_url, final String data_URL)
    {
        RequestQueue queue = Volley.newRequestQueue(SyncRecordList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("poly_Exec", response);
                        if (response.equals("\"inserted\"")) {
                            URL_Exec(data_url,"Data",xid);

                            poly_response=response;
                            Log.i("poly_Exec", poly_response);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                progressDialog.dismiss();

                Log.i("onpostexecute","caught here");
                mTask.cancel(true);
                pDialog1.dismiss();
                Toast.makeText(SyncRecordList.this,"Some error occured\tTry after sometime",Toast.LENGTH_SHORT).show();

                poly_response=String.valueOf(error);
                if (type.equals("Data")) {
                    Log.i("URL_Exec_Data/Polygon", String.valueOf(error));
                }

                Log.i("URL_Exec_Data/Polygon", String.valueOf(error));

            }
        }
        );
        queue.add(stringRequest);
       // Log.i("poly_response",poly_response);
        return poly_response;
    }
    private void createList() {
        mSQLiteHelper = new SQLiteHelper(this, "Layouts.sqlite", null, 1);
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM Layouts");
        employees.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String Owner = cursor.getString(11);
            String Fathername = cursor.getString(12);
            String Phoneno = cursor.getString(14);
            //add to list
            employees.add(new SyncModel(id, Owner, Owner, Fathername,Phoneno));
        }
        adapter.notifyDataSetChanged();
    }
    public void back(View view) {
        Intent intent = new Intent(SyncRecordList.this, NavigationView.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SyncRecordList.this, NavigationView.class);
        startActivity(intent);
    }
}
