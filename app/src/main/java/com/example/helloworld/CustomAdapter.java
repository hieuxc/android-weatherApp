package com.example.helloworld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_item, null);

        TextView txtDayOfWeek = convertView.findViewById(R.id.textViewDayOfWeek);
        TextView txtDay = convertView.findViewById(R.id.textViewDay);
        TextView txtTemp = convertView.findViewById(R.id.textViewTemp);
        TextView txtStatus = convertView.findViewById(R.id.textViewStatus);
        ImageView imgIcon = convertView.findViewById(R.id.imageViewIcon);

        Weather weather=arrayList.get(position);
        txtDayOfWeek.setText(weather.DayOfWeek);
        txtDay.setText(weather.Day);
        txtTemp.setText(weather.Temp);
        txtStatus.setText(weather.Status);
        Picasso.get().load("http://openweathermap.org/img/wn/" + weather.ImageIcon + "@4x.png").into(imgIcon);

        return convertView;
    }
}
