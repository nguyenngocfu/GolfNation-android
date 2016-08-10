package com.golf.golfnation.game;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.golf.golfnation.MainActivity;
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
public class MyGameAdapter extends BaseAdapter {
    private Context context;
    private List<Game> games = new ArrayList<Game>();
    public MyGameAdapter(Context ctx) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHoder = new ViewHolder();
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lv_mygame_element, null, false);
            viewHoder.btnView = (Button) view.findViewById(R.id.btn_view);
            viewHoder.tvDateTime = (TextView) view.findViewById(R.id.date_time);
            viewHoder.tvName = (TextView) view.findViewById(R.id.name);
            viewHoder.tvCost = (TextView) view.findViewById(R.id.cost);
            viewHoder.view = view;
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHolder)view.getTag();
        }
        viewHoder.tvName.setText(games.get(i).getName());
        int cost = 0;
        try {
            double dcost = Double.valueOf(games.get(i).getCost());
            cost = (int) dcost;
        } catch (Exception e) {};
        viewHoder.tvCost.setText("$"+cost);
        SimpleDateFormat apiDateFormat = new SimpleDateFormat(Constants.Format.DATE_API_FORMAT);
        SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.Format.DATE_DISPLAY_FORMAT);
        SimpleDateFormat apiTimeFormat = new SimpleDateFormat(Constants.Format.TIME_API_FORMAT);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat(Constants.Format.TIME_DISPLAY_FORMAT);
        try {
            Date apiDate = apiDateFormat.parse(games.get(i).getDate());
            Date apiTime = apiTimeFormat.parse(games.get(i).getTime());
            viewHoder.tvDateTime.setText("Date:" + displayFormat.format(apiDate) + " Time:" + displayTimeFormat.format(apiTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHoder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment detailFragment = GameDetailFragment.newInstance(games.get(i));
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        viewHoder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment detailFragment = GameDetailFragment.newInstance(games.get(i));
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public static class ViewHolder {
        TextView tvName;
        TextView tvDateTime;
        TextView tvCost;
        Button btnView;
        View view;
    }
}
