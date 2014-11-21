package com.example.todolist;

import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ActionBarActivity {

	DBAccess access;
	ListView lv;
	SimpleCursorAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.lvTodo);
		access = new DBAccess(this, "schedule", null, 2);
		access.add("2014-11-22", "0317", "第三");
		access.add("2014-11-21", "0517", "蔡依林");
	}

	@Override
	protected void onResume() {
		Cursor c = access.getData("date>=strftime('%Y-%m-%d','now')",
				"date desc,time");
		if (adapter == null) {
			adapter = new SimpleCursorAdapter(this, R.layout.list_item, c,
					new String[] { "date", "time", "title" }, new int[] {
							R.id.tvDate, R.id.tvTime, R.id.tvTitle });
			lv.setAdapter(adapter);
		} else {
			adapter.changeCursor(c);
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
