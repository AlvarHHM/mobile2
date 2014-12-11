package edu.mobile.assignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;
import java.util.Locale;

import edu.mobile.assignment.data.DBHelper;


public class MainActivity extends Activity {

    private NumberPicker semesterPicker;
    private NumberPicker weekPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Locale.setDefault(Locale.ENGLISH);

        semesterPicker = (NumberPicker) findViewById(R.id.semester_picker);
        weekPicker = (NumberPicker) findViewById(R.id.week_picker);

        semesterPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        weekPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        semesterPicker.setMinValue(1);
        semesterPicker.setMaxValue(2);

        weekPicker.setMinValue(1);
        weekPicker.setMaxValue(12);
    }

    public void enterDay(View view) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_today:
                intent = new Intent(this, LectureListActivity.class);
                intent.putExtra("day", day - 1);
                intent.putExtra("semester", semesterPicker.getValue());
                intent.putExtra("week", weekPicker.getValue());
                startActivity(intent);
                break;
            case R.id.btn_tomorrow:
                intent = new Intent(this, LectureListActivity.class);
                intent.putExtra("day", (day + 1));
                intent.putExtra("semester", semesterPicker.getValue());
                intent.putExtra("week", weekPicker.getValue());
                startActivity(intent);
                break;
        }
    }

    public void selectDay(View view) {
        new SelectDayDialogFragment().show(getFragmentManager(), "");
    }

    public void dataEntry(View view) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        Intent intent = new Intent(this, LectureInputActivity.class);
        intent.putExtra("day", day - 1);
        intent.putExtra("semester", semesterPicker.getValue());
        intent.putExtra("week", weekPicker.getValue());
        startActivity(intent);
    }

    public void selectModule(View view) {
        new SelectModuleDialogFragment().show(getFragmentManager(), "");
    }

    public static class SelectModuleDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select Module");
            DBHelper dbHelper = new DBHelper(getActivity(), null);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            final Cursor c = db.rawQuery("select distinct module,_id from lecture", null);
            builder.setAdapter(new SimpleCursorAdapter(getActivity(),
                            android.R.layout.simple_list_item_1, c,
                            new String[]{"module"}, new int[]{android.R.id.text1},
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            c.moveToPosition(i);
                            Intent intent = new Intent(getActivity(), LectureFileActivity.class);
                            intent.putExtra("module", c.getString(0));
                            startActivity(intent);
                        }
                    });

            return builder.create();
        }
    }

    public static class SelectDayDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select day")
                    .setItems(R.array.array_day, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NumberPicker semesterPicker = (NumberPicker) getActivity()
                                    .findViewById(R.id.semester_picker);
                            NumberPicker weekPicker = (NumberPicker) getActivity()
                                    .findViewById(R.id.week_picker);
                            Intent intent = new Intent(getActivity(), LectureListActivity.class);
                            intent.putExtra("day", which + 1);
                            intent.putExtra("semester", semesterPicker.getValue());
                            intent.putExtra("week", weekPicker.getValue());
                            startActivity(intent);

                        }
                    });
            return builder.create();
        }
    }

}
