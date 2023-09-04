package com.gisfy.unauthorizedlayouts.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper{

    //constructor
    public SQLiteHelper(Context context,
                        String name,
                        SQLiteDatabase.CursorFactory factory,
                        int version){
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public  void createtable(){
        SQLiteDatabase database = getWritableDatabase();

        String query = "CREATE TABLE IF NOT EXISTS Mandals(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MandalName VARCHAR)";

        database.execSQL(query);

        query="CREATE TABLE IF NOT EXISTS Villages(MandalId INTEGER, VillageName VARCHAR, FOREIGN" +
                " KEY(MandalId) REFERENCES Mandals(id))";

        database.execSQL(query);
    }


    public long insertMandal(String mandal){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MandalName",mandal);
        return database.insert("Mandals",null,cv);
    }

    public boolean insertVillage(long mandalId, String villageName){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MandalId",mandalId);
        cv.put("VillageName",villageName);
        long rowId =database.insert("Villages",null,cv);

        if(rowId==-1)
            return false;
        else
            return true;
    }

    public Cursor getMandals(){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery("SELECT * FROM Mandals",null);
    }

    public Cursor getVillages(int mandalId){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery("SELECT * FROM Villages WHERE MandalId="+mandalId,null);
    }

    public void deleteMandalsAndVillages(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Mandals",null,null);
        database.delete("Villages",null,null);
    }

    //insertData
    public void insertData(String Draftsman,String District,String Ulb,String village,String Dno,String Locality,String StreetName,String Doorno,
                           String Extent,String Plots,String Owner,String Fathername,String Address1,
                           String Phoneno,String lat,String lng, String Notes,int empid,String timestamp,String imagepath,String uploadid,String imagepath2,String uploadid2,String imagepath3,String uploadid3,String imagepath4,String uploadid4,String noofplots,
                           String Latlngs,String polygon,String Videopath,String videoname,
                           String mandal){
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO Layouts VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ? ,?,?,?,?,?,?,?,?,?,?,?,?)"; //where "RECORD" is table name
        // in database we will create in mainActivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, Draftsman);
        statement.bindString(2, District);
        statement.bindString(3, Ulb);
        statement.bindString(4, village);
        statement.bindString(5, Dno);
        statement.bindString(6, Locality);
        statement.bindString(7, StreetName);
        statement.bindString(8, Doorno);
        statement.bindString(9, Extent);
        statement.bindString(10, Plots);
        statement.bindString(11, Owner);
        statement.bindString(12, Fathername);
        statement.bindString(13, Address1);
        statement.bindString(14, Phoneno);
        statement.bindString(15, lat);
        statement.bindString(16, lng);
        statement.bindString(17, Notes);
        statement.bindString(18, String.valueOf(empid));
        statement.bindString(19, timestamp);
        statement.bindString(20, imagepath);
        statement.bindString(21, uploadid);
        statement.bindString(22, imagepath2);
        statement.bindString(23, uploadid2);
        statement.bindString(24, imagepath3);
        statement.bindString(25, uploadid3);
        statement.bindString(26, imagepath4);
        statement.bindString(27, uploadid4);
        statement.bindString(28, noofplots);
        statement.bindString(29, Latlngs);
        statement.bindString(30, polygon);
        statement.bindString(31, Videopath);
        statement.bindString(32, videoname);
        statement.bindString(33,mandal);
        statement.executeInsert();
    }

    //updateData
    public void updateData(String Draftsman,String District,String Ulb,String village,String Dno,String Locality,String StreetName,String Doorno,
                           String Extent,String Plots,String Owner,String Fathername,String Address1,
                           String Phoneno,String lat,String lng, String Notes ,String imagepath,
                           String imagepath2,String imagepath3,String imagepath4,String noofplots
            ,String videopath,String editpolygon,String mandal,int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to update record

        String sql = "UPDATE Layouts SET Draftsman=?, District=?, Ulb=?, Village=?, Sno=?,Locality=?, StreetName=?, DoorNo=?,Extent=?, Plots=?, OwnerName=?,FathersName=?, Address1=?, PhoneNo=?, Latitude=?,Longitude=?,Notes=?, imagepath=?,imagepath2=?,imagepath3=?,imagepath4=?, noofplots=?, videopath=?, polygon=?, Mandal=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, Draftsman);
        statement.bindString(2, District);
        statement.bindString(3, Ulb);
        statement.bindString(4, village);
        statement.bindString(5, Dno);
        statement.bindString(6, Locality);
        statement.bindString(7, StreetName);
        statement.bindString(8, Doorno);
        statement.bindString(9, Extent);
        statement.bindString(10, Plots);
        statement.bindString(11, Owner);
        statement.bindString(12, Fathername);
        statement.bindString(13, Address1);
        statement.bindString(14, Phoneno);
        statement.bindString(15, lat);
        statement.bindString(16, lng);
        statement.bindString(17, Notes);
        statement.bindString(18, imagepath);
        statement.bindString(19, imagepath2);
        statement.bindString(20, imagepath3);
        statement.bindString(21, imagepath4);
        statement.bindString(22, noofplots);
        statement.bindString(23, videopath);
        statement.bindString(24, editpolygon);
        statement.bindString(25,mandal);
        statement.bindDouble(26, (double)id);

        statement.execute();
        database.close();
    }

    //deleteData
    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM Layouts WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);
        statement.execute();
        database.close();
    }
    public void deleteTable(){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM Layouts";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.execute();
        database.close();
    }
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}