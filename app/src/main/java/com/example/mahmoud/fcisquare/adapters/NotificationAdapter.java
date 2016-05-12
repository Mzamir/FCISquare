package com.example.mahmoud.fcisquare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mahmoud.fcisquare.R;
import com.example.mahmoud.fcisquare.model.Notification;

import java.util.List;

/**
 * Created by Mahmoud on 5/10/2016.
 */
public class NotificationAdapter extends BaseAdapter {
    Context context;
    List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.notification_fragment, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.notificationText);
            viewHolder.date = (TextView) convertView.findViewById(R.id.notificationDate);
            viewHolder.time = (TextView) convertView.findViewById(R.id.notificationTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(notificationList.get(position).getText());
        viewHolder.time.setText(notificationList.get(position).getTime());
        viewHolder.date.setText(notificationList.get(position).getDate());
        return convertView;
    }

    private class ViewHolder {
        TextView text;
        TextView date;
        TextView time;
    }
}