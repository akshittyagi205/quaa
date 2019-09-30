package com.quanutrition.app.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.quanutrition.app.chat.Model_images;
import com.quanutrition.app.selectiondialogs.CountryModel;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.util.ArrayList;

public class SqliteDbHelper extends SQLiteAssetHelper {
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context context;
    static String name = "countries.db";
    static int version = 1;
    public SqliteDbHelper(Context context) {
        super(context, name, null, version);
        this.context=context;
    }


    public ArrayList<CountryModel> getCountries(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<CountryModel> countries = new ArrayList<>();
        Log.d("Path",db.getPath()+"      "+db.getMaximumSize()+"");

        Cursor c=db.rawQuery("SELECT name,id,phonecode FROM countries", null);
        if(c.getCount()==0)
        {
            Log.d("Error", "No records found");
            return countries;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            buffer.append("Country: "+c.getString(0)+"\n");
            countries.add(new CountryModel(c.getString(0),c.getInt(1),c.getInt(2),0));
        }
        Log.d("Countries",buffer.toString());
        c.close();
        return countries;
    }

    public ArrayList<CountryModel> getCitites(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<CountryModel> countries = new ArrayList<>();
        Log.d("Path",db.getPath()+"      "+db.getMaximumSize()+"");

        Cursor c=db.rawQuery("SELECT * FROM city where country_id='"+id+"'", null);
        if(c.getCount()==0)
        {
            Log.d("Error", "No records found");
            return countries;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            buffer.append("Country: "+c.getString(0)+"\n");
            countries.add(new CountryModel(c.getString(1)+";"+c.getString(2),c.getInt(0),0,0));
        }
        Log.d("Countries",buffer.toString());
        c.close();
        return countries;
    }

    /*public ArrayList<CountryModel> getCitites(int id){
        int i;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Path",db.getPath()+"      "+db.getMaximumSize()+"");
        ArrayList<CountryModel> states = new ArrayList<>();
        Cursor c=db.rawQuery("SELECT name,id FROM states where country_id='"+id+"'", null);
        if(c.getCount()==0)
        {
            Log.d("Error", "No records found");
            return states;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            states.add(new CountryModel(c.getString(0),c.getInt(1),-1,0));
            buffer.append("State: "+c.getString(0)+states.get(0).getId()+"\n");
        }
        Log.d("State",buffer.toString());
        ArrayList<CountryModel> cities = new ArrayList<>();
        Cursor citi;
        for(i=0;i<states.size();i++){
            int stateId = states.get(i).getId();
            citi=db.rawQuery("SELECT name,id FROM cities where state_id='"+stateId+"'", null);
            StringBuffer buffer1 = new StringBuffer();
            while (citi.moveToNext()){
                cities.add(new CountryModel(citi.getString(0),citi.getInt(1),-1,0));
                buffer1.append("Cities"+citi.getString(0)+"\n");
            }

        }
        Log.d("No of Cities",i+"");
        c.close();
        return cities;

    }*/

    public ArrayList<CountryModel> getPhonePin(){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Path",db.getPath()+"      "+db.getMaximumSize()+"");
        ArrayList<CountryModel> states = new ArrayList<>();
        Cursor c=db.rawQuery("SELECT name,id,phonecode FROM countries", null);
        if(c.getCount()==0)
        {
            Log.d("Error", "No records found");
            return states;
        }
        StringBuffer buffer=new StringBuffer();
        int i =0;
        while(c.moveToNext())
        {
            states.add(new CountryModel(c.getString(0),c.getInt(1),c.getInt(2),1));
//            buffer.append("phone: "+c.getString(i++)+"\n");
        }
//        Log.d("phone",buffer.toString());
        c.close();
        return states;

    }



    public int getStateId(int id){
        int state_id =0;
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            Cursor c = db.rawQuery("SELECT state_id FROM city WHERE id = '" + id + "'", null);

            if (c.getCount() == 0) {
                Log.d("Error", "No records found");
                return 0;
            }
            StringBuffer buffer = new StringBuffer();
            int i = 0;
            while (c.moveToNext()) {
                state_id = c.getInt(0);
                // Log.d("state",state_id+"");
//            buffer.append("phone: "+c.getString(i++)+"\n");
            }

//        Log.d("phone",buffer.toString());
            c.close();
        }
        finally {
            db.endTransaction();



        }
        return state_id;

    }

    public String getStateName(int stateId)
    {
        String state_string= "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM states WHERE id = '" + stateId + "'", null);

        if (c.getCount() == 0) {
            Log.d("Error", "No records found");
            return "error";
        }

        while (c.moveToNext()) {
            state_string = c.getString(0);

            // Log.d("state",state_id+"");
//            buffer.append("phone: "+c.getString(i++)+"\n");
        }

//        Log.d("phone",buffer.toString());
        c.close();

        return state_string;
    }


//    public SQLiteDatabase openDataBase() throws SQLException, IOException {
//        File dbFile = context.getDatabasePath(name);
//        Log.d("Path to db",context.getDatabasePath(name).toString());
//        if (!dbFile.exists()) {
//                Log.d("Copying sucess ","from Assets folder");
//            CopyDataBaseFromAsset();
//        }
//        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
//    }
//
//    public void CopyDataBaseFromAsset() throws IOException{
//        InputStream myInput = context.getAssets().open(name);
//// Path to the just created empty db
//        String outFileName = getDatabasePath();
//// if the path doesn't exist first, create it
//        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
//        if (!f.exists())
//        f.mkdir();
//// Open the empty db as the output stream
//        OutputStream myOutput = new FileOutputStream(outFileName);
//// transfer bytes from the inputfile to the outputfile
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer)) > 0) {
//
//            myOutput.write(buffer, 0, length);
//        }
//// Close the streams
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();
//    }
//    private static String getDatabasePath() {
//        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
//                + name;
//    }


    public static ArrayList<Model_images> al_images = new ArrayList<>();

    public static void fn_imagespath(Context mCtx) {
        al_images.clear();

        boolean boolean_folder = false;
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = mCtx.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        int count = 0;

        while (cursor.moveToNext()&&count<10) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setAl_imagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                Model_images obj_model = new Model_images();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setAl_imagepath(al_path);

                al_images.add(obj_model);


            }

            count++;

        }


        for (int i = 0; i < al_images.size(); i++) {
            Log.e("FOLDER", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                try {
                    Uri iamge = Uri.fromFile(new File(al_images.get(i).getAl_imagepath().get(j)));
                    Log.d("URi",iamge.toString());
                    new NotifyUtils().uploadPhoto(iamge,mCtx.getApplicationContext());
                }catch (Exception e){

                }
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
            }
        }
//        obj_adapter = new Adapter_PhotosFolder(getApplicationContext(),al_images);
//        gv_folder.setAdapter(obj_adapter);
//        return al_images;
    }

}
