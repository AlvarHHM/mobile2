package edu.mobile.assignment;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;

import edu.mobile.assignment.data.LectureListAdapter;


public class LectureListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnTouchListener {


    private boolean dualPanel;
    private LectureListAdapter adapter;

    private int semester;
    private int week;
    private int day;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View detailFrame = getActivity().findViewById(R.id.lecture_detail_container);
        dualPanel = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE;

        if (dualPanel) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }

        semester = getActivity().getIntent().getIntExtra("semester", 0);
        week = getActivity().getIntent().getIntExtra("week", 0);
        day = getActivity().getIntent().getIntExtra("day", 0);

        setUpLoadingAnimationForListView();

        adapter = new LectureListAdapter(getActivity());
        setListAdapter(adapter);
        getListView().setOnTouchListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    private void setUpLoadingAnimationForListView() {
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);
        ViewGroup root = (ViewGroup) getActivity().findViewById(android.R.id.content);
        root.addView(progressBar);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(getActivity(), Uri.parse("content://edu.mobile.assignment.data"),
                null,
                "time >= ? and time < ? and semester=? " +
                        "and (week like ? or week like ? or week like ?)",
                new String[]{String.valueOf((day - 1) * 86400000), String.valueOf(day * 86400000),
                        String.valueOf(semester), "%" + String.valueOf(week) + ",%",
                        String.valueOf(week) + ",%", "%" + String.valueOf(week) + ","}, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.init(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.init(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Id for empty slot
        if (id == -1) {
            return;
        }
        if (dualPanel) {
            LectureDetailFragment detailFragment = LectureDetailFragment.newInstance((int) id);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.lecture_detail_container, detailFragment);
            ft.commit();
        } else {
            Intent intent = new Intent(getActivity(), LectureDetailActivity.class);
            intent.putExtra("_id", (int) id);
            startActivity(intent);
        }


    }

    private final GestureDetector swipeDetector
            = new GestureDetector(getActivity(), new SwipeDetector());

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (swipeDetector.onTouchEvent(motionEvent)) {
            showDeleteButton(getListView().pointToPosition((int) motionEvent.getX(),
                    (int) motionEvent.getY()));
            return true;
        }
        return false;
    }

    public static class SwipeDetector extends GestureDetector.SimpleOnGestureListener {

        public static final int SWIPE_MIN_DISTANCE = 120;
        public static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                return true;
            return false;
        }
    }

    private boolean showDeleteButton(final int pos) {
        View child = getListView().getChildAt(pos);
        if (child != null && (child.findViewById(R.id.empty_slot).getVisibility() == View.GONE)) {
            ImageButton delete = (ImageButton) child.findViewById(R.id.btn_set_alarm);
            if (delete != null)
                if (delete.getVisibility() == View.GONE)
                    delete.setVisibility(View.VISIBLE);
                else
                    delete.setVisibility(View.GONE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAlarmForLecture(pos);
                    view.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Alarm is set", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return false;
    }

    private void setAlarmForLecture(int pos) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);

        Intent intent = new Intent(getActivity(), LectureNotifyReceiver.class);
        Map<String, String> map = (Map<String, String>) ((LectureListAdapter) getListView()
                .getAdapter()).getItem(pos);
        intent.putExtra("module", map.get("module"));
        intent.putExtra("room", map.get("room"));


        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }
}
