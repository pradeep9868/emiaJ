package co.jlabs.jaime.jaime;

//Created by pradeep kumar (Jussconnect)

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Jaime";
    private static final String FILE_DIR = "PraJaimeCache";
    Context context;
    private static final String TABLE_Image = "image_name";
    private static final String KEY_ID = "id";
    private static final String KEY_JAIME = "sess_name";
    private static final String KEY_IMAGENAME = "imagename";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
 		String CREATE_JAIME_TABLE = "CREATE TABLE "+TABLE_Image+" ( " +
                KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_JAIME+" TEXT, " +
                KEY_IMAGENAME+" TEXT);";
 		db.execSQL(CREATE_JAIME_TABLE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Image);
        this.onCreate(db);
	}
	//---------------------------------------------------------------------

	/**
     * CRUD operations (create "add", read "get", update, delete)
     */



    public void addBarocde(Class_Image tp){

		SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_JAIME, tp.sess_name);
        values.put(KEY_IMAGENAME, tp.imagename);
        db.insert(TABLE_Image,null,values);
        values.clear();
        db.close();
    }

    public ArrayList<String> getAllImage(String sess_name) {
    	ArrayList<String> Images = new ArrayList<>();
        String query = "SELECT  "+KEY_IMAGENAME+" FROM " + TABLE_Image + " where "+KEY_JAIME+"='"+sess_name+"' ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {
        	
        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            do {
                Images.add(cursor.getString(0));
                Log.i("Db","OK "+cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
        return Images;
    }

    public ArrayList<String> getAllSess_Name() {
        ArrayList<String> Sess_Name = new ArrayList<>();
        String query = "SELECT  DISTINCT("+KEY_JAIME+") FROM " + TABLE_Image + " ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        String tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new String(cursor.getString(0));
                Sess_Name.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return Sess_Name;
    }
    public boolean deleterow(String imgname)
    {
        Log.i("Db", "Del " + imgname);
        SQLiteDatabase db = this.getWritableDatabase();

        int b=0;
        try {
            b = db.delete(TABLE_Image, KEY_IMAGENAME + " = '" + imgname + "'", null);
        //  b = db.delete(TABLE_Image, KEY_IMAGENAME + " like ?", new String[]{imgname});
        //    Log.i("Db", " eeee 6 " + b);
        }
        catch(Exception e)
        {
            Log.i("Db", "Error db ");
            e.printStackTrace();
        }
        db.close();
        if(b>0)
            return true;
        else
            return false;
    }
}
