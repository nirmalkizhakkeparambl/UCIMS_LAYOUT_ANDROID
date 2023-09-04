package com.gisfy.unauthorizedlayouts.BottomNavigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gisfy.unauthorizedlayouts.BottomNavigation.adapter.BottomAdapter;
import com.gisfy.unauthorizedlayouts.BottomNavigation.design.BadgeBottomNavigtion;
import com.gisfy.unauthorizedlayouts.BottomNavigation.model.BottomItem;
import com.gisfy.unauthorizedlayouts.DashBoard;
import com.gisfy.unauthorizedlayouts.Profile.GuideMe;
import com.gisfy.unauthorizedlayouts.Profile.Help;
import com.gisfy.unauthorizedlayouts.Profile.Profile;
import com.gisfy.unauthorizedlayouts.Profile.aboutus;
import com.gisfy.unauthorizedlayouts.R;


public class NavigationView extends AppCompatActivity implements BottomAdapter.BottomItemClickInterface {
    private BadgeBottomNavigtion badgeBottomNavigtion;
    Fragment fragment;
    private final int HOME = 0;
    private final int PROFILE = 1;
    private final int ABOUT = 2;
    private final int HELP=3;
    private int selectedId = 0;
    AlertDialog dialog;
    private static final String[] LOCATION_AND_CONTACTS =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS};

    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);
        badgeBottomNavigtion = new BadgeBottomNavigtion(findViewById(R.id.BottomNavigation), NavigationView.this, NavigationView.this);
        initBottomItems();
    }

    @SuppressLint("ResourceType")
    private void initBottomItems() {
        BottomItem home = new BottomItem(HOME, R.drawable.home, "Home", true);
        BottomItem Profile = new BottomItem(PROFILE, R.drawable.ic_account_circle_black_24dp, "Profile", true);
        BottomItem About = new BottomItem(ABOUT, R.drawable.about, "About Us", false);
        BottomItem Help =new BottomItem(HELP,R.drawable.help,"Help", false);
        badgeBottomNavigtion.addBottomItem(home);
        badgeBottomNavigtion.addBottomItem(Profile);
        badgeBottomNavigtion.addBottomItem(About);
        badgeBottomNavigtion.addBottomItem(Help);
        badgeBottomNavigtion.apply(selectedId, getString(R.color.selectedColor), getString(R.color.unselectedColor));
        itemSelect(selectedId);
    }
    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(NavigationView.this);
        builder.setMessage("Do you want to exit?");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
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
    @Override
    public void itemSelect(int itemId)
    {

       switch(itemId)
       {
           case 0:
               fragment= new DashBoard();
                break;
           case 1:
               fragment = new Profile();
              break;
           case 2:
               fragment = new aboutus();
               break;
           case 3:
               fragment=new Help();
               break;
       }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.commit();
        }
    }

    public void guideme(View view) {
        startActivity(new Intent(NavigationView.this, GuideMe.class));
    }
}
