package com.golf.golfnation.game;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.golf.golfnation.R;
import com.golf.golfnation.game.model.Game;
import com.golf.golfnation.game.model.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Nguyen on 2/4/2015.
 */
public class MembersAdapter extends BaseAdapter {
    private Context context;
    private List<Member> members = new ArrayList<Member>();
    public MembersAdapter(Context ctx) {
        context = ctx;
    }

    public void setData(List<Member> games) {
        this.members = games;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
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
            view = inflater.inflate(R.layout.lv_member_element, null, false);
            viewHoder.tvName = (TextView)view.findViewById(R.id.player1_name);
            viewHoder.tvPrize = (TextView)view.findViewById(R.id.amount);
            viewHoder.cbWin = (CheckBox)view.findViewById(R.id.cb_p1_win);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHolder)view.getTag();
        }
        viewHoder.tvName.setText(members.get(i).getRealUserName());
        viewHoder.tvPrize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    members.get(i).setPrize(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    Log.e("Format number", e.getMessage());
                }
            }
        });
        viewHoder.cbWin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                members.get(i).setWinner(isChecked);
            }
        });
        return view;
    }

    public static class ViewHolder {
        TextView tvName;
        TextView tvPrize;
        CheckBox cbWin;
    }
}
