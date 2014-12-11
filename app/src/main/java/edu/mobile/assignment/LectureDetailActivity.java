package edu.mobile.assignment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class LectureDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            LectureDetailFragment details = LectureDetailFragment
                    .newInstance(getIntent().getIntExtra("_id", -1));
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }

    }
}
