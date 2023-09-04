package com.gisfy.unauthorizedlayouts.Profile;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.unauthorizedlayouts.R;

import static android.content.Context.MODE_PRIVATE;


public class Profile  extends Fragment {

    public Profile() {
        // Required empty public constructor
    }

    TextView txempID,txempname,txempplace,txempworking,txempmobile,txempwhatsapp,txempmail,txempdesignationid,txempdesignationname,txempuserid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        txempID=view.findViewById(R.id.txt_empID);
        txempname=view.findViewById(R.id.txt_empName);
        txempplace=view.findViewById(R.id.ULB);
        txempmobile=view.findViewById(R.id.txt_empmbno);
        txempwhatsapp=view.findViewById(R.id.designationname);
        txempmail=view.findViewById(R.id.txt_empmail);
        txempuserid=view.findViewById(R.id.txt_empuserid);
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String empName = sh.getString("employeename", "");
        String empULB = sh.getString("ulbname", "");
        String empEmail = sh.getString("emailid", "");
        String empDesignationname = sh.getString("designationname", "");
        String empUserID = sh.getString("userid", "");
        String empMobile = sh.getString("phoneno", "");
        int empID = sh.getInt("employeeid", 0);
        int empDesignationID = sh.getInt("designationid", 0);

        int empULBCode = sh.getInt("ulbcode", 0);

        if (!(empName==null&&empULB==null&&empDesignationname==null&&empEmail==null)) {

            txempplace.setText(empULB);
            txempmail.setText(empEmail);
            txempID.setText("");
            txempname.setText(empName);
            txempmobile.setText(empMobile);
            txempwhatsapp.setText((empDesignationname));
            txempuserid.setText(empUserID);
            Log.i("sharedpref", empName + empEmail);
        }
        return view;
    }
}
