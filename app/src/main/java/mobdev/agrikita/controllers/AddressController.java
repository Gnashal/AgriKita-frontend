package mobdev.agrikita.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.models.address.Address;

public class AddressController {
    private final DBHelper dbHelper;
    private static AddressController instance;
    public AddressController(Context context) {
        dbHelper = new DBHelper(context);
    }
    public static AddressController getInstance(Context context) {
        if (instance == null) {
            instance = new AddressController(context);
        }
        return instance;
    }

    public long insertAddress(Address address, String uid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UID", uid);
        values.put("Name", address.getName());
        values.put("Phone", address.getPhone());
        values.put("IsDefault", address.isDefault() ? 1 : 0);
        values.put("Region", address.getRegion());
        values.put("Province", address.getProvince());
        values.put("City", address.getCity());
        values.put("Barangay", address.getBarangay());
        values.put("StreetName", address.getStreetName());
        values.put("ZipCode", address.getZipCode());
        values.put("Label", address.getLabel());
        long id = db.insert(DBHelper.TABLE_NAME, null, values);
        values.put("DbIndex", id);
        return id;
    }

    public List<Address> getAddressesForUid(String uid) {
        List<Address> addressList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, "UID = ?", new String[]{uid}, null, null, null);

        while (cursor.moveToNext()) {
            Address address = new Address(
                    cursor.getString(cursor.getColumnIndexOrThrow("Phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("IsDefault")) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow("Region")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Province")),
                    cursor.getString(cursor.getColumnIndexOrThrow("City")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Barangay")),
                    cursor.getString(cursor.getColumnIndexOrThrow("StreetName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ZipCode")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Label"))
            );
            addressList.add(address);
        }

        cursor.close();
        return addressList;
    }

    public Address getDefaultAddressForUid(String uid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, "UID = ? AND IsDefault = 1", new String[]{uid}, null, null, null);
        if (cursor.moveToFirst()) {
            Address address = new Address(
                    cursor.getString(cursor.getColumnIndexOrThrow("Phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                    true,
                    cursor.getString(cursor.getColumnIndexOrThrow("Region")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Province")),
                    cursor.getString(cursor.getColumnIndexOrThrow("City")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Barangay")),
                    cursor.getString(cursor.getColumnIndexOrThrow("StreetName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ZipCode")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Label"))
            );
            cursor.close();
            return address;
        }
        cursor.close();
        return null;
    }

    public long updateAddress(Address address, long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Phone", address.getPhone());
        values.put("Name", address.getName());
        values.put("IsDefault", address.isDefault() ? 1 : 0);
        values.put("Region", address.getRegion());
        values.put("Province", address.getProvince());
        values.put("City", address.getCity());
        values.put("Barangay", address.getBarangay());
        values.put("StreetName", address.getStreetName());
        values.put("ZipCode", address.getZipCode());
        values.put("Label", address.getLabel());
        return db.update(DBHelper.TABLE_NAME, values, "ID = ?", new String[]{String.valueOf(id)});
    }

    public long deleteAddress(String uid, long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_NAME, "ID = ?", new String[]{String.valueOf(id)});

    }

    static class DBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "agrikita_db";
        private static final int VERSION = 1;
        private static final String TABLE_NAME = "Addresses";

        private static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "UID TEXT, " +
                        "Name TEXT, " +
                        "Phone TEXT, " +
                        "IsDefault INTEGER, " +
                        "Region TEXT, " +
                        "Province TEXT, " +
                        "City TEXT, " +
                        "Barangay TEXT, " +
                        "StreetName TEXT, " +
                        "ZipCode TEXT, " +
                        "Label TEXT" +
                        ")";

        public DBHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Schema migration logic can go here
        }
    }
}
