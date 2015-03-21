package wartech.kampusid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakim on 11-Feb-15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "kampusManager",
    TABLE_NAME = "kampus",
    KEY_ID = "id",
    KEY_NAME = "name",
    KEY_PHONE = "phone",
    KEY_EMAIL = "email",
    KEY_ADDRESS = "address",
    KEY_IMAGEURI = "imageUri";

    public DatabaseHandler(Context contect)
    {
        super(contect, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_PHONE + " TEXT," + KEY_EMAIL + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_IMAGEURI + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);

        onCreate(db);
    }

    public void Create(ContactActivity contact)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGEURI, contact.getImage().toString());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ContactActivity getKampus(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,new String[] {KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_ADDRESS, KEY_IMAGEURI}, KEY_ID + " =? ", new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        ContactActivity contact = new ContactActivity(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4), Uri.parse(cursor.getString(5)));
        db.close();
        cursor.close();
        return contact;
    }

    public void delete(ContactActivity contact)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID + " =? ",new String[] {String.valueOf(contact.getId()) });
        db.close();
    }

    public int getListCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public int Update(ContactActivity contact)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGEURI, contact.getImage().toString());
        int rowsAffected = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[] {String.valueOf(contact.getId())} );
        db.close();

        return rowsAffected;
    }

    public List<ContactActivity> getAll()
    {
        List<ContactActivity> contacts = new ArrayList<ContactActivity>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(cursor.moveToFirst())
        {
            ContactActivity contact;
            do {
                contacts.add(new ContactActivity(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return contacts;
    }
}
