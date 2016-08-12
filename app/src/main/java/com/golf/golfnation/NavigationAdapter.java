package com.golf.golfnation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.golf.golfnation.common.view.CircleTransform;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngọc Nguyễn on 12/1/2014.
 */
public class NavigationAdapter extends BaseAdapter {
    private Context context;

    public NavigationAdapter(Context ctx) {
        context = ctx;
    }

    private List<NavigationItem> menuItems = new ArrayList<>();

    public void setData(List<NavigationItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    public void updateItem(NavigationItem item, int index){
        if((index < menuItems.size()) && index >=0 ) {
            menuItems.get(index).setImageURL(item.getImageURL());
            menuItems.get(index).setTitle(item.getTitle());
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int i) {
        return menuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHoder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.navigation_item, null, false);
        viewHoder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        viewHoder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        view.setTag(viewHoder);
        viewHoder.ivIcon.setImageResource(menuItems.get(i).getResId());
        viewHoder.tvTitle.setText(menuItems.get(i).getTitle());

        if(menuItems.get(i).getImageURL() != null) {
            Picasso.with(context).load(menuItems.get(i).getImageURL()).memoryPolicy(MemoryPolicy.NO_CACHE).transform(new CircleTransform()).into(viewHoder.ivIcon);
        }
        return view;
    }

    public static class ViewHolder {
        public static ImageView ivIcon;
        public static TextView tvTitle;
    }
}
