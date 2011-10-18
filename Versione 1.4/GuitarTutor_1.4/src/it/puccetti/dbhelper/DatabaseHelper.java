package it.puccetti.dbhelper;

import android.content.ContentValues;

import android.content.Context;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteQueryBuilder;

import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	static final String TAG = "sambasync/DatabaseHelper";

	static final String DATABASE_NAME = "virtual.db";

	static final int DATABASE_VERSION = 1;

	static final String USER_TABLE = "utenti";

	static final String GROUPS_TABLE = "gruppi";

	SQLiteDatabase db;

	public DatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		db = getWritableDatabase();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.d(TAG, "DatabaseHelper onCreate called");

		db.execSQL("CREATE TABLE " + USER_TABLE + " ("

		+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"

		+ "nome TEXT,"

		+ "cognome TEXT,"

		+ "userid TEXT,"

		+ "password TEXT,"

		+ "indirizzo TEXT,"

		+ "telefono TEXT,"

		+ "datascad DATE"

		+ ");");

		db.execSQL("CREATE TABLE " + GROUPS_TABLE + " ("

		+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"

		+ "group_id TEXT,"

		+ "user_id INTEGER"

		+ ");");

	}

	@Override
	public synchronized void close() {

		db.close();

		super.close();

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(TAG, "database upgrade requested, ignored.");

		if (oldVersion == 1 && newVersion == 2) {

			db.execSQL("CREATE TABLE " + GROUPS_TABLE + " ("

			+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"

			+ "group_id TEXT,"

			+ "user_id INTEGER,"

			+ ");");

		}

	}

	public void insertNewEntry(ContentValues values, String oszTable) {

		Log.d(TAG, "insertNewConnection: " + values);

		Long rowid = db.insert(oszTable, "", values);

		if (rowid < 0) {

			Log.e(TAG, "database insert failed: " + rowid);

		} else {

			Log.d(TAG, "database insert success, rowid=" + rowid);

		}

	}

	public int getCount(Cursor c) {

		Log.d(TAG, "getCount()");

		try {

			Log.d(TAG, "count=" + c.getCount());

			return c.getCount();

		} catch (Exception e) {

			Log.e(TAG, "database exception: " + e);

			return -1;

		}

	}

	public Cursor queryAll() {

		Log.d(TAG, "queryAll()");

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(DatabaseHelper.USER_TABLE);

		return qb.query(db, null, null, null, null, null, null);

	}
}
