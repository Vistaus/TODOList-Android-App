package com.koller.lukas.todolist;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.koller.lukas.todolist.Util.ThemeHelper;
import com.koller.lukas.todolist.Todolist.Event;

import java.util.ArrayList;

/**
 * Created by Lukas on 25.08.2016.
 */
public class ImportListViewAdapter extends ArrayAdapter<Event> {
    private ThemeHelper helper;

    public boolean eventsImport = true;

    public Context context;

    public ImportListViewAdapter(Context context, ArrayList<Event> events, ThemeHelper helper) {
        super(context, 0, events);
        this.helper = helper;

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event e = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.widget_list_row, parent, false);
        }
        int color_index = e.getColor();

        TextView text = (TextView) convertView.findViewById(R.id.list_row_text);

        text.setText(e.getWhatToDo());
        if(eventsImport){
            text.getBackground().setColorFilter(helper.getEventColor_semitransparent(color_index), PorterDuff.Mode.SRC_IN);
            text.setTextColor(helper.getEventTextColor_semitransparent(color_index));
        } else {
            text.getBackground().setColorFilter(helper.getEventColor(color_index), PorterDuff.Mode.SRC_IN);
            text.setTextColor(helper.getEventTextColor(color_index));
        }
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

        float scale = context.getResources().getDisplayMetrics().density;
        int padding = (int) (20 * scale + 0.5f);
        text.setPadding(padding, padding, padding, padding);

        return convertView;
    }
}
