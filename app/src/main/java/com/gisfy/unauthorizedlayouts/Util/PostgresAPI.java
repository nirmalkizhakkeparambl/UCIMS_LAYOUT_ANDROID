package com.gisfy.unauthorizedlayouts.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gisfy.unauthorizedlayouts.SQLite.MainActivity;

import java.util.ArrayList;


public class PostgresAPI extends AsyncTask<Void,String,String> {
    JSONParser jParser;
    String latitude,  longitude,  WPRSBISAD,  Name,  FHName,  Address,  viltown,
     dno,  locality,  streetname,  ULB,  district, Extent,  Plots,Address2,Address3,SNo,PhoneNo,Notes;
    public void pushdata(String latitude, String longitude, String WPRSBISAD, String Name, String FHName, String Address,String  viltown,
                         String dno, String locality, String streetname, String ULB, String district,
                         String Extent, String Plots,String Address2,String Address3,String SNo,String PhoneNo,String Notes)
    {
        this.latitude=latitude;
        this.longitude=longitude;
        this.WPRSBISAD=WPRSBISAD;
        this.Name=Name;
        this.FHName=FHName;
        this.viltown=viltown;
        this.dno=dno;
        this.locality=locality;
        this.streetname=streetname;
        this.ULB=ULB;
        this.district=district;
        this.Extent=Extent;
        this.Plots=Plots;
        this.Address2=Address2;
        this.Address3=Address3;
        this.SNo=SNo;
        this.PhoneNo=PhoneNo;
        this.Notes=Notes;
        this.Address=Address;

    }
        String url = new String();
        ProgressDialog pd;
        String response = new String();
        String result;
         Context mainActivity;

    @Override
    protected String doInBackground(Void... arg0) {
        try {
            JSONObject json = getJsonObject(url);

            response = json.getString("Status");
            Log.i("arindam",response);

            if (response.equalsIgnoreCase("Success")) {

                result = "success";
            }
        } catch (Exception e)
        {
            result = "connection error";
        }
        return result;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();
        ArrayList<String> list = new ArrayList<String>();
        list.add(latitude);
        list.add(longitude);
        list.add(WPRSBISAD);
        list.add(Name);
        list.add(FHName);
        list.add(Address);
        list.add(viltown);
        list.add(dno);
        list.add(locality);
        list.add(streetname);
        list.add(WPRSBISAD);
        list.add(streetname);
        list.add(district);
        list.add(Extent);
        list.add(Plots);
        list.add(Address2);
        list.add(Address3);
        list.add(SNo);
        list.add(PhoneNo);
        list.add(Notes);
            url = "http://148.72.208.177/UMSAPI/api/UMS?query=INSERT INTO public.\"tblUnauthorizedLayout\"(\n" +
                    "latitude, longitude, \"WPRSBISAD\", \"Name\", \"FHName\", \"Address\", viltown, \"DoorNo\", locality, streetname, \"ULB\", district, \"Extent\", \"Plots\", \"Address1\", \"Address2\", \"SurveyNo\", \"PhoneNo\", \"Note\",\"employeeid\")\n" +
                    "VALUES ("+""+latitude+","+longitude+""
                    +WPRSBISAD+","+Name+
                    ""+FHName+","+Address+""+viltown+","+dno+""+locality+","+streetname+""+ULB+","+district+""+Extent+","+Plots+
                    ""+Address2+","+Address3+""+SNo+","+PhoneNo+""+Notes+")";
            jParser = new JSONParser();
            pd = new ProgressDialog(mainActivity);
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.show();
        }

    @Override
    protected void onPostExecute(String o) {
        if (o.equalsIgnoreCase("success"))
        {

            pd.dismiss();
            Toast.makeText(mainActivity, "finished", Toast.LENGTH_SHORT).show();

        } else {
            pd.dismiss();
            Toast.makeText(mainActivity,
                    "Network not aviliable, please try later.", Toast.LENGTH_LONG).show();
            //mainActivity.finish();
        }
        super.onPostExecute(o);
    }

    public JSONObject getJsonObject(String json){
        try {
            return new JSONObject(json);
        } catch (JSONException e) {}
        return null;
    }


}