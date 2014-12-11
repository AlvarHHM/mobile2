package edu.mobile.assignment.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.CursorAdapter;

public class LectureContentProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "edu.mobile.assignment.data";

    public static final String PATH = "lecture";

    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY
            + "/" + PATH;
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + CONTENT_AUTHORITY
            + "/" + PATH;

    private SQLiteDatabase db;

    private DBHelper dbHelper;

    public LectureContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        return CONTENT_TYPE_DIR;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("No need to implemented");
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(), null);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("lecture", null, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("No need to implemented");
    }
}
