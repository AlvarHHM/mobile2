package edu.mobile.assignment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import edu.mobile.assignment.data.DBHelper;


public class LectureInputActivity extends Activity {

    private AutoCompleteTextView moduleText;
    private Spinner roomSpinner;
    private Spinner typeSpinner;
    private AutoCompleteTextView lecturerText;
    private AutoCompleteTextView weekText;
    private Spinner daySpinner;
    private Spinner timeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_input);
        moduleText = (AutoCompleteTextView) findViewById(R.id.input_module);
        lecturerText = (AutoCompleteTextView) findViewById(R.id.input_lecturer);
        weekText = (AutoCompleteTextView) findViewById(R.id.input_week);
        roomSpinner = (Spinner) findViewById(R.id.input_spinner_room);
        typeSpinner = (Spinner) findViewById(R.id.input_spinner_type);
        daySpinner = (Spinner) findViewById(R.id.input_spinner_day);
        timeSpinner = (Spinner) findViewById(R.id.input_spinner_time);

        typeSpinner.setAdapter(ArrayAdapter.createFromResource(this,R.array.array_type,
                android.R.layout.simple_spinner_dropdown_item));
        daySpinner.setAdapter(ArrayAdapter.createFromResource(this,R.array.array_day,
                android.R.layout.simple_spinner_dropdown_item));
        timeSpinner.setAdapter(ArrayAdapter.createFromResource(this,R.array.array_time,
                android.R.layout.simple_spinner_dropdown_item));
        DBHelper dbHelper = new DBHelper(this,null);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from room",null);
        roomSpinner.setAdapter(new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,c,
                new String[]{"room"},new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));

    }


    public void submit(View view){
        DBHelper dbHelper = new DBHelper(this,null);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into lecture(module,room,type,lecturer,semester,week,time) " +
                "values (?,?,?,?,?,?,?)",new String[]{
                moduleText.getText().toString(),
                String.valueOf(roomSpinner.getSelectedItemPosition()),
                String.valueOf(typeSpinner.getSelectedItem()),
                lecturerText.getText().toString(),
                String.valueOf(getIntent().getIntExtra("semester", 1)),
                weekText.getText().toString(),
                String.valueOf((daySpinner.getSelectedItemPosition() * 86400000)
                        + ((timeSpinner.getSelectedItemPosition() + 9) * 3600000))
        });
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();


        finish();
    }
}
