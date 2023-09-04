package com.gisfy.unauthorizedlayouts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class FirstFragment extends Fragment {
    ImageView img;
    TextView tx1,tx3,tx2;
    Animation animbottom,animtop,animlfr,animrtl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_first, container, false);


        img=v.findViewById(R.id.imageView2);
        animlfr= AnimationUtils.loadAnimation(getContext(),
                R.anim.lefttoright);
        animrtl=AnimationUtils.loadAnimation(getContext(),R.anim.righttoleft);
        img.setVisibility(View.VISIBLE);
        img.startAnimation(animlfr);
        tx3=v.findViewById(R.id.textView3);
        animbottom= AnimationUtils.loadAnimation(getContext(),
                R.anim.frombottom);
        tx3.setVisibility(View.VISIBLE);
        tx3.startAnimation(animbottom);
        tx1=v.findViewById(R.id.textView);
        animtop= AnimationUtils.loadAnimation(getContext(),
                R.anim.fromtop);
        tx1.setVisibility(View.VISIBLE);
        tx1.startAnimation(animtop);

        return v;
    }


}
