package com.example.app_for_rightech_iot_cloud;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<NotificationsForRecycler> notifications;

    DataAdapter(Context context, List<NotificationsForRecycler> notifications) {
        this.notifications = notifications;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getString("theme", "light").equals("dark")){
            View view = inflater.inflate(R.layout.list_recycler_view_dark, parent, false);
            return new ViewHolder(view);
        }
        else{
            View view = inflater.inflate(R.layout.list_recycler_view, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        NotificationsForRecycler notification = notifications.get(position);
        holder.imageView.setImageResource(notification.getImage());
        holder.indicatorView.setText(notification.getIndicator());
        holder.paramView.setText(notification.getParam());
        holder.dateView.setText(notification.getDate());
        holder.warningImageView.setImageResource(notification.getWarningImage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView, warningImageView;
        final TextView indicatorView, dateView, paramView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            indicatorView = view.findViewById(R.id.indicator);
            dateView = view.findViewById(R.id.date_and_time);
            paramView = view.findViewById(R.id.param);
            warningImageView = view.findViewById(R.id.warning);
        }
    }
}