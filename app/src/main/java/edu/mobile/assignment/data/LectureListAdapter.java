package edu.mobile.assignment.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import edu.mobile.assignment.R;


public class LectureListAdapter extends BaseAdapter {

    private final Context context;

    public LectureListAdapter(Context context) {
        this.context = context;

    }

    private HashMap<String, String>[] lectureArray = new HashMap[9];

    public void init(Cursor cursor) {
        lectureArray = new HashMap[9];
        if (cursor == null) {
            return;
        }
        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.add(Calendar.MILLISECOND, (int) cursor.getLong(cursor.getColumnIndexOrThrow("time")));
                int slot = cal.get(Calendar.HOUR_OF_DAY) - 9;
                HashMap<String, String> prop = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    prop.put(cursor.getColumnName(i), cursor.getString(i));
                }
                lectureArray[slot] = prop;
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int i) {
        return lectureArray[i];
    }

    @Override
    public long getItemId(int i) {
        return lectureArray[i] != null ? Long.valueOf(lectureArray[i].get("_id")) : -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_card, viewGroup, false);
        }
        HashMap<String, String> prop = lectureArray[i];
        TextView moduleText = (TextView) view.findViewById(R.id.list_module);
        TextView lecturerText = (TextView) view.findViewById(R.id.list_lecturer);
        TextView roomText = (TextView) view.findViewById(R.id.list_room);
        TextView timeText = (TextView) view.findViewById(R.id.list_time);
        timeText.setText(String.valueOf((i + 9)) + ":00");
        if (lectureArray[i] != null) {
            moduleText.setText(prop.get("module"));
            lecturerText.setText(prop.get("lecturer"));
            roomText.setText(prop.get("room"));

            view.findViewById(R.id.empty_slot).setVisibility(View.GONE);
        } else {
            moduleText.setText("");
            lecturerText.setText("");
            roomText.setText("");

            view.findViewById(R.id.empty_slot).setVisibility(View.VISIBLE);
        }

        return view;
    }
}
