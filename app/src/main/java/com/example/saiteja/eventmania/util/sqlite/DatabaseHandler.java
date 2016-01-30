package com.example.saiteja.eventmania.util.sqlite;

/**
 * Created by Sai Teja on 12/12/2015.
 */;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.saiteja.eventmania.model.Event;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "eventhub";

    // Contacts table name
    private static final String TABLE_NAME = "events";

    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_SUBTYPE = "subtype";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATETIME = "date_time";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DESC = "description";
    private static final String KEY_CNAME1 = "cname1";
    private static final String KEY_CNO1 = "cno1";
    private static final String KEY_CNAME2 = "cname2";
    private static final String KEY_CNO2 = "cno2";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_CLUB = "club";
    private static final String KEY_GOING = "going";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE  TABLE " +TABLE_NAME+ " ("+KEY_ID+" INTEGER PRIMARY KEY  NOT NULL , "+KEY_TYPE+" VARCHAR, "+KEY_SUBTYPE+" VARCHAR, "+KEY_NAME+" TEXT UNIQUE , "+KEY_DATETIME+" DATETIME, "+KEY_IMAGE+" TEXT, "+KEY_CONTENT+" TEXT, "+KEY_DESC+" TEXT, "+KEY_CNAME1+" TEXT, "+KEY_CNO1+" TEXT, "+KEY_CNAME2+" TEXT, "+KEY_CNO2+" TEXT, "+KEY_VENUE+" TEXT, "+KEY_CLUB+" VARCHAR, "+KEY_GOING+" INTEGER DEFAULT 0)";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, event.getType());
        values.put(KEY_SUBTYPE, event.getSubtype());
        values.put(KEY_NAME, event.getEventName());
        values.put(KEY_DATETIME, event.getEventTime());
        values.put(KEY_IMAGE, event.getImagePath());
        values.put(KEY_CONTENT, event.getContent());
        values.put(KEY_DESC, event.getDescription());
        values.put(KEY_CNAME1, event.getCname1());
        values.put(KEY_CNO1, event.getCno1());
        values.put(KEY_CNAME2, event.getCname2());
        values.put(KEY_CNO2, event.getCno2());
        values.put(KEY_VENUE, event.getEventVenue());
        values.put(KEY_CLUB, event.getClub());
        values.put(KEY_GOING, event.getGoing());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME,KEY_DATETIME,KEY_IMAGE,KEY_CONTENT,KEY_DESC,KEY_CNAME1,KEY_CNO1,KEY_CNAME2,KEY_CNO2,KEY_VENUE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null,null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        Event event = new Event();
        event.setId(Integer.parseInt(cursor.getString(0)));
        event.setEventName(cursor.getString(1));
        event.setEventTime(cursor.getString(2));
        event.setImagePath(cursor.getString(3));
        event.setContent(cursor.getString(4));
        event.setImagePath(cursor.getString(5));
        event.setDescription(cursor.getString(6));
        event.setCname1(cursor.getString(7));
        event.setCno1(cursor.getString(8));
        event.setCname2(cursor.getString(9));
        event.setCno2(cursor.getString(10));
        event.setEventVenue(cursor.getString(11));
        // return contact
        return event;
    }

    public List<Event> getEventsListProfile()
    {
        List<Event> eventList=new ArrayList<>();

        String selectQuery = "SELECT "+KEY_ID+","+KEY_NAME+","+KEY_DATETIME+","+KEY_VENUE+ " FROM " + TABLE_NAME +" WHERE "+KEY_GOING+"=1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setEventName(cursor.getString(1));
                event.setEventTime(cursor.getString(2));
                event.setEventVenue(cursor.getString(3));

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }
    public List<Event> getEventsListToday()
    {
        List<Event> eventList=new ArrayList<>();

        String selectQuery = "SELECT "+KEY_ID+","+KEY_NAME+","+KEY_DATETIME+","+KEY_VENUE+ "FROM " + TABLE_NAME +" where going=1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setEventName(cursor.getString(1));
                event.setEventTime(cursor.getString(2));
                event.setEventVenue(cursor.getString(3));

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }

    public List<Event> getEventsListUpComing()
    {
        List<Event> eventList=new ArrayList<>();

        String selectQuery = "SELECT "+KEY_ID+","+KEY_NAME+","+KEY_DATETIME+","+KEY_VENUE+ "FROM " + TABLE_NAME +" where going=1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setEventName(cursor.getString(1));
                event.setEventTime(cursor.getString(2));
                event.setEventVenue(cursor.getString(3));

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }


    // Updating single contact
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TYPE, event.getType()); // Contact Name
        values.put(KEY_SUBTYPE, event.getSubtype());
        values.put(KEY_NAME, event.getEventName());
        values.put(KEY_DATETIME, event.getEventTime());
        values.put(KEY_IMAGE, event.getImagePath());
        values.put(KEY_CONTENT, event.getContent());
        values.put(KEY_DESC, event.getDescription());
        values.put(KEY_CNAME1, event.getCname1());
        values.put(KEY_CNO1, event.getCno1());
        values.put(KEY_CNAME2, event.getCname2());
        values.put(KEY_CNO2, event.getCno2());
        values.put(KEY_VENUE, event.getEventVenue());
        values.put(KEY_CLUB, event.getClub());
        values.put(KEY_GOING, event.getGoing());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
    }

    // Deleting single contact
    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }


    // Getting contacts Count
    /*
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    */

}