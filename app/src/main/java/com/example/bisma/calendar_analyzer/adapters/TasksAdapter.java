package com.example.bisma.calendar_analyzer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.interfaces.TaskEditCallback;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devprovider on 16/08/2017.
 */

public class TasksAdapter extends ArrayAdapter<EventModelDep> {

    private Context context;
    private List<EventModelDep> dataListStored;
    private List<EventModelDep> dataListDisplayed;
    private TaskEditCallback callback;

    public TasksAdapter(Context context, List<EventModelDep> dataList, TaskEditCallback callback){
        super(context, -1, dataList);
        this.context = context;
        this.dataListStored = dataList;
        this.dataListDisplayed = dataList;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return dataListDisplayed.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public EventModelDep getItem(int position) {
        return dataListDisplayed.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.tasks_list_item, parent, false);
            ((TextView) convertView.findViewById(R.id.task_title_tv)).setText(dataListDisplayed.get(position).getEventTitle());
            ((ImageView) convertView.findViewById(R.id.edit_task_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onTaskEditClicked(dataListDisplayed.get(position));
                }
            });
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<EventModelDep> filteredArrList = new ArrayList<>();

                if (TextUtils.isEmpty(constraint)) {
                    results.count = dataListStored.size();
                    results.values = dataListStored;
                } else {
                    String query = constraint.toString().toLowerCase();
                    for (int i = 0; i < dataListStored.size(); i++) {
                        String match = dataListStored.get(i).getEventTitle().toString().toLowerCase();
                        if (match.startsWith(query) || match.contains(query)) {
                            filteredArrList.add(dataListStored.get(i));
                        }
                    }
                    results.count = filteredArrList.size();
                    results.values = filteredArrList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataListDisplayed = (ArrayList<EventModelDep>) results.values;
                notifyDataSetChanged();


            }
        };
        return filter;
    }
}
