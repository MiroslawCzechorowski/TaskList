package com.example.czechorowski.tasklist;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by MiroslawCzechorowski on 18.11.2015.
 */
public class CustomCursorAdapter extends SimpleCursorAdapter {
    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int pos=cursor.getPosition()+1;
        TextView rowCount=(TextView)view.findViewById(R.id.rowCountTextView);
        rowCount.setText(pos+". ");
        super.bindView(view, context, cursor);
    }


}
