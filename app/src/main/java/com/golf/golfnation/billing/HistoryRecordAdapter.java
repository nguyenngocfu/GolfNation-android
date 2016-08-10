package com.golf.golfnation.billing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.golf.golfnation.R;
import com.golf.golfnation.billing.model.HistoryRecord;
import com.golf.golfnation.game.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Nguyen on 2/4/2015.
 */
public class HistoryRecordAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryRecord> records = new ArrayList<HistoryRecord>();
    public HistoryRecordAdapter(Context ctx) {
        context = ctx;
    }

    public void setData(List<HistoryRecord> games) {
        this.records = games;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
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
            view = inflater.inflate(R.layout.lv_order_history_element, null, false);
            viewHoder.tvOrderNo = (TextView) view.findViewById(R.id.order_number);
            viewHoder.tvDate = (TextView) view.findViewById(R.id.date_created);
            viewHoder.tvTime = (TextView) view.findViewById(R.id.time_created);
            viewHoder.tvCost = (TextView) view.findViewById(R.id.total);
            viewHoder.tvStatus = (TextView) view.findViewById(R.id.status);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHolder)view.getTag();
        }
        viewHoder.tvOrderNo.setText(records.get(i).getOrderNo());
        viewHoder.tvDate.setText(records.get(i).getDate());
        viewHoder.tvTime.setText(records.get(i).getTime());
        viewHoder.tvCost.setText(records.get(i).getCost());
        viewHoder.tvStatus.setText(records.get(i).getStatus());

        return view;
    }

    public static class ViewHolder {
        TextView tvOrderNo;
        TextView tvDate;
        TextView tvTime;
        TextView tvCost;
        TextView tvStatus;
    }
}
