package com.golf.golfnation.billing;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.golf.golfnation.Golfnation;
import com.golf.golfnation.R;
import com.golf.golfnation.billing.model.HistoryRecord;
import com.golf.golfnation.billing.model.HistoryResponse;
import com.golf.golfnation.common.Constants;
import com.golf.golfnation.common.RequestManager;
import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.game.GameAdapter;
import com.golf.golfnation.game.model.Game;
import com.golf.golfnation.game.model.MembersResponse;
import com.golf.golfnation.user.model.UserDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment implements Constants{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lvHistories;
    private HistoryRecordAdapter adapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFragment newInstance() {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        requestHistoryTransactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order_history,container, false);
        lvHistories = (ListView) rootView.findViewById(R.id.lv_orderhistory);
        adapter = new HistoryRecordAdapter(getActivity());
        lvHistories.setAdapter(adapter);
        return rootView;
    }

    ProgressDialog pDialog;
    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        if(show) {
            if(pDialog == null)
                pDialog = ProgressDialog.show(getActivity(), "","Loading..", false);
            else
                pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }
    
    // Get game listing
    public void requestHistoryTransactions() {
        UserDetail userDetail = ((Golfnation)getActivity().getApplication()).getUserDetail();
        String url = String.format(ORDER_LIST_URL, userDetail.getUser_id());
        StringRequest myGameRequest = new StringRequest(Request.Method.GET, url, onCompleteListener, onErrorListener);
        RequestManager.getInstance(getActivity()).getRequestQueue().add(myGameRequest);
        showProgress(true);
    }

    private Response.Listener<String> onCompleteListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            if(Constants.Status.STATUS_OK.equals(baseResponse.getStatus())) {
                try {
                    HistoryResponse historyResponse = gson.fromJson(response, HistoryResponse.class);
                    adapter.setData(historyResponse.getDetails());
                } catch (Exception e) {

                }
            } else {
                Toast.makeText(getActivity(), baseResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };
    private Response.ErrorListener onErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            showProgress(false);
        }
    };


}
