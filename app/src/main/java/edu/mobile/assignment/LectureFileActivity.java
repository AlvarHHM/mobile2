package edu.mobile.assignment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public class LectureFileActivity extends FragmentActivity {

    private ViewPager viewPager;
    private Button addFileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_file);

        addFileButton = (Button) findViewById(R.id.btn_add_file);
        viewPager = (ViewPager) findViewById(R.id.file_pager_container);
        viewPager.setAdapter(new FilePagerAdapter(getFragmentManager()));
        setupActionbar();
    }

    private void setupActionbar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        for (int i = 0; i < 12; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Week " + (i + 1))
                            .setTabListener(tabListener));
        }
    }

    public void addFile(View view) {
        Intent intent = new Intent(getApplicationContext(), ExDialog.class);
        intent.putExtra("explorer_title",
                getString(R.string.dialog_read_from_dir));
        intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String module = getIntent().getStringExtra("module");
            int week = viewPager.getCurrentItem() + 1;
            SharedPreferences sharedPreferences =
                    getSharedPreferences(module + week, Activity.MODE_PRIVATE);
            Uri uri = data.getData();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(new File(uri.toString()).getName(), uri.toString());
            editor.commit();
        }
    }

    public class FilePagerAdapter extends FragmentStatePagerAdapter {

        public FilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LectureFileFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Week " + (position + 1);
        }
    }

    public static class LectureFileFragment extends ListFragment {

        private ArrayAdapter<String> adapter;
        private Map map;

        public static LectureFileFragment newInstance(int week) {
            LectureFileFragment fragment = new LectureFileFragment();
            Bundle args = new Bundle();
            args.putInt("week", week);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, new ArrayList<String>());
            setListAdapter(adapter);
            loadFileList();

        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse((String) map.get(adapter.getItem(position))), "*/*");
            startActivity(intent);
        }

        @Override
        public void onResume() {
            super.onResume();
            loadFileList();
        }

        private void loadFileList() {
            String module = getActivity().getIntent().getStringExtra("module");
            int week = getArguments().getInt("week");
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(module + week, Activity.MODE_PRIVATE);
            map = sharedPreferences.getAll();
            ArrayList<String> fileNameArray = new ArrayList<String>();
            for (Object key : map.keySet()) {
                String fileName = (String) key;
                fileNameArray.add(fileName);
            }
            adapter.clear();
            adapter.addAll(fileNameArray);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
