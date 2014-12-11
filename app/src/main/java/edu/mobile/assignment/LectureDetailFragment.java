package edu.mobile.assignment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import edu.mobile.assignment.data.DBHelper;

public class LectureDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private int _id;
    private TextView moduleText;
    private TextView roomText;
    private TextView typeText;
    private TextView lecturerText;
    private TextView semesterText;
    private TextView weekText;
    private TextView timeText;
    private ImageView mapImage;


    public static LectureDetailFragment newInstance(int _id) {
        LectureDetailFragment fragment = new LectureDetailFragment();
        Bundle args = new Bundle();
        args.putInt("_id",_id);
        fragment.setArguments(args);
        return fragment;
    }

    public LectureDetailFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _id = getArguments().getInt("_id");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lecture_detail, container, false);
        moduleText = (TextView) rootView.findViewById(R.id.text_detail_module);
        roomText = (TextView) rootView.findViewById(R.id.text_detail_room);
        typeText = (TextView) rootView.findViewById(R.id.text_detail_type);
        lecturerText = (TextView) rootView.findViewById(R.id.text_detail_lecturer);
        semesterText = (TextView) rootView.findViewById(R.id.text_detail_semester);
        weekText = (TextView) rootView.findViewById(R.id.text_detail_week);
        timeText = (TextView) rootView.findViewById(R.id.text_detail_time);
        mapImage = (ImageView) rootView.findViewById(R.id.detail_map);

        getLoaderManager().initLoader(0,null,this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i("",_id+"");
        return new CursorLoader(getActivity(), Uri.parse("content://edu.mobile.assignment.data"),
                null,"_id = ?",
                new String[]{String.valueOf(getArguments().getInt("_id"))},null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if(cursor.moveToFirst()){
            moduleText.setText(cursor.getString(cursor.getColumnIndexOrThrow("module")));
            roomText.setText(cursor.getString(cursor.getColumnIndexOrThrow("room")));
            lecturerText.setText(cursor.getString(cursor.getColumnIndexOrThrow("lecturer")));
            semesterText.setText(cursor.getString(cursor.getColumnIndexOrThrow("semester")));
            weekText.setText(cursor.getString(cursor.getColumnIndexOrThrow("week")));

            switch (cursor.getInt(cursor.getColumnIndexOrThrow("type"))){
                case 0: typeText.setText("Lecture");break;
                case 1: typeText.setText("Tutorial");break;
                case 2: typeText.setText("Lab");break;
            }
            int time = cursor.getInt(cursor.getColumnIndexOrThrow("time"));
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.add(Calendar.MILLISECOND,time);
            timeText.setText(cal.get(Calendar.HOUR_OF_DAY)+":00");
            loadMapImage(roomText.getText().toString());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void loadMapImage(String room) {
        DBHelper dbHelper = new DBHelper(getActivity(),null);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select lat,lon from room where room=?",new String[]{room});
        if(c.moveToFirst()){
            final double lat =  (c.getInt(0)/Math.pow(10,6));
            final double lon =  (c.getInt(1)/Math.pow(10,6));
            new LoadImageFromURL().execute(lat,lon);
            mapImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + lat + "," + lon));
                    startActivity(intent);
                }
            });
        }

    }

    public class LoadImageFromURL extends AsyncTask<Double, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Double... params) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/staticmap?center=" + params[0] + "," + params[1] + "&zoom=15&size=300x300&sensor=false&markers=color:red%7Clabel:A%7C" + params[0] + "," + params[1]);
                InputStream is = url.openConnection().getInputStream();
                Bitmap bitMap = BitmapFactory.decodeStream(is);
                return bitMap;

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
            mapImage.setImageBitmap(result);
        }

    }

}
