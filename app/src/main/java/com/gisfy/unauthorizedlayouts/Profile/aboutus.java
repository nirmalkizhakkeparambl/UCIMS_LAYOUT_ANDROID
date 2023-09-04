package com.gisfy.unauthorizedlayouts.Profile;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gisfy.unauthorizedlayouts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class aboutus extends Fragment {


    public aboutus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aboutus, container, false);
    }

}
