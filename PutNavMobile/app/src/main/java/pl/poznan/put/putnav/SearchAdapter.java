package pl.poznan.put.putnav;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<SearchItem> {

    Context context;
    ArrayList<SearchItem> data = null;
    int resource;


    public SearchAdapter(Context context, int resource, ArrayList<SearchItem> data) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SearchHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new SearchHolder();
            holder.pointType = (TextView) row.findViewById(R.id.txtTitle);
            holder.pointName = (TextView) row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        } else {
            holder = (SearchHolder) row.getTag();
        }

        SearchItem searchItem = data.get(position);
        holder.pointName.setText(searchItem.getPointName());
        holder.pointType.setText(searchItem.getPointType());

        return row;
    }

    static class SearchHolder {
        TextView pointType;
        TextView pointName;
    }
}

