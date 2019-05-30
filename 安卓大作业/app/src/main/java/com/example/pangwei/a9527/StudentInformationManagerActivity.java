package com.example.pangwei.a9527;
import java.util.ArrayList;
import java.util.List;
import com.example.pangwei.a9527.MySqlHelper;
import com.example.pangwei.a9527.Student;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class StudentInformationManagerActivity extends Activity {

	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private List<Student> students = new ArrayList<Student>();
	private ListView stu_listview;
	private CheckBox checkboxsum;
	private boolean chboxall = false;
	private String updateName;
	private AutoCompleteTextView serach_edit;
	private List<String> lists = new ArrayList<String>();
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentinfomationmaneger);

		mySqlHelper = new MySqlHelper(StudentInformationManagerActivity.this,
				"student_inf.db", null, 1);
		db = mySqlHelper.getWritableDatabase();

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		checkboxsum = (CheckBox) findViewById(R.id.checkboxsum);
		checkboxsum.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				chboxall = arg1;
				stu_adapter.notifyDataSetChanged();
			}
		});

		stu_listview = (ListView) findViewById(R.id.stu_listview);
		stu_listview.setAdapter(stu_adapter);
		stu_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				showInformation(arg2);
				System.out.println(arg2);
			}
		});

	}

	public void addStudent() {
		Intent intent = new Intent(StudentInformationManagerActivity.this,
				AddStudentActivity.class);
		startActivity(intent);

	}

	public void delStudent() {

		for (Student student : students) {
			System.out.println(student.isChecked());
			if (student.isChecked()) {
				Cursor cursor = db.rawQuery("delete from student where id = '"
						+ student.getId() + "'", null);
				cursor.moveToNext();
			}
		}

		// Cursor cursor =
		// db.rawQuery("delete from student where id = '"+id+"'", null);
		// cursor.moveToNext();
		onResume();
		// chboxall = false;
	}



	public void showInformation(final int temp) {
		Builder builder = new Builder(StudentInformationManagerActivity.this);
		LayoutInflater inflater = LayoutInflater
				.from(StudentInformationManagerActivity.this);
		View view = inflater.inflate(R.layout.showstudentinf, null);
		final AlertDialog dialog = builder.setTitle("学生详细信息").setView(view)
				.create();
		Button sxiugai = (Button) view.findViewById(R.id.sxiugai);
		Button sok = (Button) view.findViewById(R.id.sok);
		Button scancel = (Button) view.findViewById(R.id.scancel);
		final EditText sname = (EditText) view.findViewById(R.id.sname);
		final EditText ssex = (EditText) view.findViewById(R.id.ssex);
		final EditText smingzu = (EditText) view.findViewById(R.id.smingzu);
		final EditText sid = (EditText) view.findViewById(R.id.sid);
		final EditText sbir = (EditText) view.findViewById(R.id.sbir);
		final EditText sphone = (EditText) view.findViewById(R.id.sphone);
		final EditText smore = (EditText) view.findViewById(R.id.smore);
		sname.setText(students.get(temp).getName().toString());
		ssex.setText(students.get(temp).getSex().toString());
		smingzu.setText(students.get(temp).getMingZu().toString());
		sid.setText(students.get(temp).getId().toString());
		sbir.setText(students.get(temp).getBirthday().toString());
		sphone.setText(students.get(temp).getPhone().toString());
		smore.setText(students.get(temp).getMore().toString());

		Cursor cursor = db.rawQuery(
				"select * from student where id = '"
						+ students.get(temp).getId().toString() + "'", null);
		// 知道只有一条记录 直接moveToNext
		cursor.moveToNext();
		// cursor.getColumnIndex("image")获取列的索引
		dialog.show();
		updateName = students.get(temp).getName().toString();
		sxiugai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sname.setEnabled(true);
				ssex.setEnabled(true);
				smingzu.setEnabled(true);
				sid.setEnabled(true);
				sbir.setEnabled(true);
				sphone.setEnabled(true);
				smore.setEnabled(true);
			}
		});
		sok.setOnClickListener(new OnClickListener() { //可变数据修改

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Cursor cursor = db.rawQuery("update student set name = '"
				// + sname.getText().toString() + "',sex = '"
				// + ssex.getText().toString() + "',mingzu = '"
				// + smingzu.getText().toString() + "',id = '"
				// + sid.getText().toString() + "',birthday = '"
				// + sbir.getText().toString() + "',phone = '"
				// + sphone.getText().toString() + "',more = '"
				// + smore.getText().toString() + "' ,image = '"
				// + updateName + "'", null);

				ContentValues values = new ContentValues();
				values.put("name", sname.getText().toString());
				values.put("sex", ssex.getText().toString());
				values.put("mingzu", smingzu.getText().toString());
				values.put("id", sid.getText().toString());
				values.put("birthday", sbir.getText().toString());
				values.put("phone", sphone.getText().toString());
				values.put("more", smore.getText().toString());
				db.update("student", values, "name=?", new String[]{updateName});
				//cursor.moveToNext();
				// Toast.makeText(StudentInformationManagerActivity.this,
				// "修改成功！",
				// Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		scancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

	}

	public void userMana() {
		Intent intent = new Intent(StudentInformationManagerActivity.this,
				UserManage.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		students = new ArrayList<Student>();
		Cursor cursor = db.rawQuery("select * from student", null);
		while (cursor.moveToNext()) {
			students.add(new Student(cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4), cursor
					.getString(5), cursor.getString(6), cursor
					.getString(7)));
		}
		// stu_listview.setAdapter(stu_adapter);
		stu_adapter.notifyDataSetChanged();
		super.onResume();
	}

	BaseAdapter stu_adapter = new BaseAdapter() {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(
					StudentInformationManagerActivity.this).inflate(
					R.layout.stulistviewitems, null);
			CheckBox stu_checkbox = (CheckBox) view
					.findViewById(R.id.stu_checkbox);
			TextView stu_list_name = (TextView) view
					.findViewById(R.id.stu_list_name);
			TextView stu_list_id = (TextView) view
					.findViewById(R.id.stu_list_id);
			TextView stu_list_sex = (TextView) view
					.findViewById(R.id.stu_list_sex);
			final int position = arg0;
			stu_checkbox.setChecked(chboxall);
			stu_checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
													 boolean isChecked) {
							// TODO Auto-generated method stub
							students.get(position).setChecked(isChecked);
						}
					});
			stu_list_name.setText(students.get(arg0).getName().toString());
			stu_list_id.setText(students.get(arg0).getId().toString());
			stu_list_sex.setText(students.get(arg0).getSex().toString());

			return view;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return students.size();
		}
	};



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//自定义菜单
		// TODO Auto-generated method stub
		MenuInflater inflater = new MenuInflater(
				StudentInformationManagerActivity.this);
		inflater.inflate(R.menu.contextmenu, menu);

		View searchView = menu.findItem(R.id.search).getActionView();
		ImageView search = (ImageView) searchView
				.findViewById(R.id.image_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(1111);
				// TODO Auto-generated method stub
				students = new ArrayList<Student>();
				Cursor cursor = db.rawQuery(
						"select * from student where name = '"
								+ serach_edit.getText().toString() + "'", null);
				while (cursor.moveToNext()) {
					students.add(new Student(cursor.getString(1), cursor
							.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5), cursor
							.getString(6), cursor.getString(7)));
				}
				// stu_listview.setAdapter(stu_adapter);
				Toast.makeText(StudentInformationManagerActivity.this, "搜索完毕！",
						Toast.LENGTH_SHORT).show();
				stu_adapter.notifyDataSetChanged();
			}
		});
		serach_edit = (AutoCompleteTextView) searchView
				.findViewById(R.id.search_edit);
		Cursor cursorstu = db.rawQuery("select name from student", null);
		while (cursorstu.moveToNext()) {
			lists.add(cursorstu.getString(0));
		}
		serach_edit.setThreshold(1);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				StudentInformationManagerActivity.this,
				android.R.layout.simple_list_item_1, lists);
		serach_edit.setAdapter(arrayAdapter);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.stu_add:
				addStudent();
				break;
			case R.id.stu_del:
				delStudent();
				break;
			case R.id.stu_ref:
				onResume();
				break;
			case R.id.user_ma:
				userMana();
				break;

		}
		return super.onOptionsItemSelected(item);
	}



}