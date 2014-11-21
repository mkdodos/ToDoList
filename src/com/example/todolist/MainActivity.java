package com.example.todolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ActionBarActivity {

	DBAccess access;
	ListView lv;
	SimpleCursorAdapter adapter = null;

	EditText date, time, title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.lvTodo);

		date = (EditText) findViewById(R.id.etDate);
		time = (EditText) findViewById(R.id.etTime);
		title = (EditText) findViewById(R.id.etTitle);

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());
		String timeStamp2 = new SimpleDateFormat("HHmm").format(Calendar
				.getInstance().getTime());

		date.setText(timeStamp);
		time.setText(timeStamp2);

		Button btnAdd = (Button) findViewById(R.id.btnAdd);

		access = new DBAccess(this, "schedule", null, 3);

		// 日期事件,取得焦點時,顯示日期選擇器
		date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					showDatePicker();
				}

			}
		});

		// 日期事件
		date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				showDatePicker();
			}
		});

		btnAdd.setOnClickListener(btListener);

	}

	private Calendar m_Calendar;

	private void showDatePicker() {
		// 取得月曆物件
		m_Calendar = Calendar.getInstance();

		// 日期選擇器事件
		DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

			// 做了設定日期的動作
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// 設定物件的日期
				m_Calendar.set(Calendar.YEAR, year);
				m_Calendar.set(Calendar.MONTH, monthOfYear);
				m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				// 設定日期格式,並將此文字設定給文字方塊
				String myFormat = "yyyy/MM/dd";
				SimpleDateFormat sdf = new SimpleDateFormat(myFormat,
						Locale.TAIWAN);
				date.setText(sdf.format(m_Calendar.getTime()));

			}
		};

		// 日期選擇器物件
		DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
				datepicker, m_Calendar.get(Calendar.YEAR),
				m_Calendar.get(Calendar.MONTH),
				m_Calendar.get(Calendar.DAY_OF_MONTH));

		// 隱藏日期選擇器中的月曆
		if (Build.VERSION.SDK_INT >= 11) {
			dialog.getDatePicker().setCalendarViewShown(false);
		}

		dialog.show();
	}

	private OnClickListener btListener = new OnClickListener() {
		public void onClick(View v) {
			access.add(date.getText().toString(), time.getText().toString(),
					title.getText().toString());

			Cursor c = access.getData("date>=strftime('%Y-%m-%d','now')",
					"date desc,time");
			adapter.changeCursor(c);

			date.setText("");
			time.setText("");
			title.setText("");

		}
	};

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
