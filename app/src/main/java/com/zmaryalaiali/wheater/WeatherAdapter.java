package com.zmaryalaiali.wheater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyHolder>{

    Context mContext;
    List<WeatherModel> weatherModelList;

    public WeatherAdapter(Context mContext, List<WeatherModel> weatherModelList) {
        this.mContext = mContext;
        this.weatherModelList = weatherModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(mContext).load(weatherModelList.get(position)).into(holder.ivWeatherICon);
        holder.tvWeatherInfo.setText(weatherModelList.get(position).getTempreture());
        holder.tvTempreture.setText(weatherModelList.get(position).getTempreture());
        holder.tvWindSpeed.setText(weatherModelList.get(position).getWindSpeed());


    }

    @Override
    public int getItemCount() {
        return weatherModelList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public TextView tvTempreture,tvWindSpeed,tvWeatherInfo;
        public ImageView ivWeatherICon;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tvTempreture = itemView.findViewById(R.id.tv_weather_tempreture);
            tvWeatherInfo = itemView.findViewById(R.id.tv_weather_info);
            tvWindSpeed = itemView.findViewById(R.id.tv_windSpeed);
            ivWeatherICon = itemView.findViewById(R.id.iv_weather_icon);
        }
    }
}



