package com.golf.golfnation.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.golf.golfnation.R;
import com.golf.golfnation.common.Constants;
import com.golf.golfnation.game.model.Game;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ngoc Nguyen on 2/4/2015.
 */
public class GameAdapter extends BaseAdapter {
    private Context context;
    private List<Game> games = new ArrayList<Game>();
    public GameAdapter(Context ctx) {
        context = ctx;
    }

    public void setData(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int i) {
        return games.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHoder = new ViewHolder();
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lv_game_element, null, false);
            viewHoder.tvName = (TextView)view.findViewById(R.id.name);
            viewHoder.tvAddress = (TextView)view.findViewById(R.id.address);
            viewHoder.tvCost = (TextView)view.findViewById(R.id.cost);
            viewHoder.tvDateTime = (TextView)view.findViewById(R.id.date_time);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHolder)view.getTag();
        }
        viewHoder.tvName.setText(games.get(i).getName());
        viewHoder.tvAddress.setText("Golf Course:"+games.get(i).getCourse()+", City:"+games.get(i).getCity()+", State:"+games.get(i).getState());
        SimpleDateFormat apiDateFormat = new SimpleDateFormat(Constants.Format.DATE_API_FORMAT);
        SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.Format.DATE_DISPLAY_FORMAT);
        SimpleDateFormat apiTimeFormat = new SimpleDateFormat(Constants.Format.TIME_API_FORMAT);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat(Constants.Format.TIME_DISPLAY_FORMAT);
        try {
            Date apiDate = apiDateFormat.parse(games.get(i).getDate());
            Date apiTime = apiTimeFormat.parse(games.get(i).getTime());
            viewHoder.tvDateTime.setText("Time: " + displayFormat.format(apiDate) + " " + displayTimeFormat.format(apiTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int cost = 0;
        try {
            double dcost = Double.valueOf(games.get(i).getCost());
            cost = (int) dcost;
        } catch (Exception e) {};
        viewHoder.tvCost.setText("$"+cost);

        return view;
    }

    public static class ViewHolder {
        TextView tvName;
        TextView tvAddress;
        TextView tvDateTime;
        TextView tvCost;
    }
}
