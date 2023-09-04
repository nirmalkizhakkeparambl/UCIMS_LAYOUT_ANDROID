package com.gisfy.unauthorizedlayouts.Util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallAPI extends AsyncTask<String, String, String> {

    String latitude,  longitude,  WPRSBISAD,  Name,  FHName,  Address,  viltown,
            dno,  locality,  streetname,  ULB,  district, Extent,  Plots,Address2,Address3,SNo,PhoneNo,Notes;
    Context context;
    public CallAPI(Context context, String latitude, String longitude, String WPRSBISAD, String Name, String FHName, String Address, String  viltown,
                   String dno, String locality, String streetname, String ULB, String district,
                   String Extent, String Plots, String Address2, String Address3, String SNo, String PhoneNo, String Notes){
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
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = "http://148.72.208.177/UMSAPI/api/UMS?query=INSERT INTO public.\"tblUnauthorizedLayout\"(\n" +
                "latitude, longitude, \"WPRSBISAD\", \"Name\", \"FHName\", \"Address\", viltown, \"DoorNo\", locality, streetname, \"ULB\", district, \"Extent\", \"Plots\", \"Address1\", \"Address2\", \"SurveyNo\", \"PhoneNo\", \"Note\",\"employeeid\")\n" +
                "VALUES ("+""+latitude+","+longitude+""
                +WPRSBISAD+","+Name+
                ""+FHName+","+Address+""+viltown+","+dno+""+locality+","+streetname+""+ULB+","+district+""+Extent+","+Plots+
                ""+Address2+","+Address3+""+SNo+","+PhoneNo+""+Notes+")"; // URL to call
        //String data = params[1]; //data to post
        OutputStream out = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            out = new BufferedOutputStream(urlConnection.getOutputStream());
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//            writer.write(data);
//            writer.flush();
//            writer.close();
//            out.close();

            urlConnection.connect();
        } catch (Exception e) {
//            System.out.println(e.getMessage());
           // Toast.makeText(context, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return urlString;
    }
}