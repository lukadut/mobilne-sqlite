package c.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 2016-01-20.
 */
public class DataBaseAdapter {


    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db.db";

    private static final String CREATE_DB = "CREATE TABLE [example] (\n" +
        "[id] INTEGER DEFAULT '0' NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
        "[text] VARCHAR(30)  NOT NULL\n" +
        ");\n";

    private static final String DROP_DB =
            "DROP TABLE IF EXISTS [example]";

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    public DataBaseAdapter(Context context) {
        this.context = context;
    }


    public DataBaseAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public long insertText(String text) {
        ContentValues newValues = new ContentValues();
        newValues.put("text", text);
        return db.insert("example", null, newValues);
    }

    public Text getText(long id) {
        String[] columns = {"id", "text"};
        String where = "id =" + id;
        String text=null;
        Cursor cursor = db.query("example", columns, where, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            Log.d("kolumna 0 id " , cursor.getLong(0)+"");
            Log.d("kolumna 1 text " , cursor.getString(1));
            text=cursor.getString(1);

        }
        return new Text(text);
    }

    public Cursor getAll(){
        Cursor cursor = db.rawQuery("select * from example",null);
        return cursor;
    }

    public boolean updateText(long id, String text) {
        String where = "id =" + id;
        ContentValues updateTextValues = new ContentValues();
        updateTextValues.put("text", text);
        return db.update("example", updateTextValues, where, null) > 0;
    }

    public boolean deleteText(long id){
        String where = "id =" + id;
        return db.delete("example", where, null) > 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB);
            Log.d("DataBaseAdapter", "Tworzenie bazy");
        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_DB);
            Log.d("DataBaseAdapter", "Aktualizowanie bazy");
            onCreate(db);
        }
    }
}
