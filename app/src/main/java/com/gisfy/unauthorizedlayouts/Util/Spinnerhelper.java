package com.gisfy.unauthorizedlayouts.Util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gisfy.unauthorizedlayouts.R;

public class Spinnerhelper
{
    Context mCtx;
    public Spinnerhelper(Context context)
    {
        this.mCtx=context;
    }

    public void ulbspinnerselection(String arg2, Spinner sSubClass, String Ulb, Context arg1) {

        if (arg2.equals("KURNOOL")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Kurnool));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("ANANTAPUR")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Anantapur));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("GUNTUR")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Guntur));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("PRAKASAM")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Prakasam));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("WEST GODAVARI")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.WestGodavari));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("YSR KADAPA")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.YSRKadapa));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("VISKHAPATNAM")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.GreaterVishakapatanam));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("VIZIANAGARAM")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Vizianagaram));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("KRISHNA")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Krishna));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("SRIKAKULAM")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Srikakulam));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("CHITTOOR")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Chittoor));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("EAST GODAVARI")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1,R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.EastGodavari));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
        if (arg2.equals("NELLORE")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg1, R.layout.spinner_layout, mCtx.getResources().getStringArray(R.array.Nellore));
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            sSubClass.setAdapter(adapter);
            if (Ulb != null) {
                int i = adapter.getPosition(Ulb);
                sSubClass.setSelection(i);
            }
        }
    }
}
