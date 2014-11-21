package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAccess extends SQLiteOpenHelper {

	public DBAccess(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE `todolist` ("
				+ "_id	INTEGER PRIMARY KEY AUTOINCREMENT," + "`date`	TEXT,"
				+ "`time`	TEXT," + "`title`	TEXT)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists todolist");
		onCreate(db);

	}

	long add(String date, String time, String title) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("date", date);
		values.put("time", time);
		values.put("title", title);
		long result = db.insert("todolist", null, values);
		db.close();
		return result;
	}

	Cursor getData(String whereClause, String orderBy) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query("todolist", new String[] { "_id", "date", "time",
				"title" }, whereClause, null, null,null,orderBy,"5");
		return c;
		
//		public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
	}

}
