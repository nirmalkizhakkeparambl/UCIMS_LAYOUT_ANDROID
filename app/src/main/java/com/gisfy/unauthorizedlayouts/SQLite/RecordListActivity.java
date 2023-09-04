package com.gisfy.unauthorizedlayouts.SQLite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gisfy.unauthorizedlayouts.BottomNavigation.NavigationView;
import com.gisfy.unauthorizedlayouts.Edit_Sql;
import com.gisfy.unauthorizedlayouts.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RecordListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model> mList;
    RecordListAdapter mAdapter = null;
    SQLiteHelper mSQLiteHelper;
    LinearLayout relativeLayout;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        mListView = findViewById(R.id.listView);
        relativeLayout=findViewById(R.id.relative_snack);
        ImageView iv=findViewById(R.id.back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordListActivity.this, NavigationView.class));
            }
        });
        mList = new ArrayList<>();
        client= LocationServices.getFusedLocationProviderClient(this);
        mAdapter = new RecordListAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);
        mSQLiteHelper = new SQLiteHelper(this, "Layouts.sqlite", null, 1);
        Cursor cursor =mSQLiteHelper.getData("SELECT * FROM Layouts");
        mList.clear();
        while (cursor.moveToNext())
        {
            String Owner = cursor.getString(11);
            String Fathername = cursor.getString(12);
            String Phoneno = cursor.getString(14);
            mList.add(new Model(Owner,Fathername,Phoneno));
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size()==0){
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "No Data Found..?", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Add Now", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(RecordListActivity.this,MainActivity.class));
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"Update", "Delete","Cancel"};
                final AlertDialog.Builder dialog = new AlertDialog.Builder(RecordListActivity.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            //update
                            Cursor c =mSQLiteHelper.getData("SELECT id FROM Layouts");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog
                            Intent intent = new Intent(getBaseContext(), Edit_Sql.class);
                            intent.putExtra("position", arrID.get(position));
                            startActivity(intent);

                        }
                        if (i==1){
                            //delete
                            Cursor c = mSQLiteHelper.getData("SELECT id FROM Layouts");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                        if (i==2)
                        {
                            dialogInterface.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }





    private void showDialogDelete(final int idRecord)
    {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(RecordListActivity.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(RecordListActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
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
        mAdapter.notifyDataSetChanged();
    }

}