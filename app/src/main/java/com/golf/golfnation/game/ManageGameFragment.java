package com.golf.golfnation.game;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.golf.golfnation.Golfnation;
import com.golf.golfnation.R;
import com.golf.golfnation.common.Constants;
import com.golf.golfnation.common.RequestManager;
import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.game.model.Member;
import com.golf.golfnation.game.model.MembersResponse;
import com.golf.golfnation.user.model.UserDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageGameFragment extends Fragment implements Constants {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView tvCost;
    private ListView lvMembers;
    private MembersAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ManageGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageGameFragment newInstance(String gameID, String cost) {
        ManageGameFragment fragment = new ManageGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, gameID);
        args.putString(ARG_PARAM2, cost);
        fragment.setArguments(args);
        return fragment;
    }

    public ManageGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_manage_game, container, false);
        tvCost = (TextView) v.findViewById(R.id.tv_cost);
        lvMembers = (ListView) v.findViewById(R.id.lv_members);
        adapter = new MembersAdapter(getActivity());
        lvMembers.setAdapter(adapter);

        Button btnPaywinner = (Button) v.findViewById(R.id.btn_pay);
        btnPaywinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestManageWinner();
            }
        });

        requestMembers();
        return v;
    }

    // Get game listing
    public void requestMembers() {
        String url = String.format(GAME_MEMBER_URL, mParam1);
        StringRequest myGameRequest = new StringRequest(Request.Method.GET, url, onCompleteListener, onErrorListener);
        RequestManager.getInstance(getActivity()).getRequestQueue().add(myGameRequest);
        showProgress(true);
    }

    // Get game listing
    public void requestManageWinner() {
        // Verify prize
        StringBuilder winners = new StringBuilder();
        StringBuilder sbPrize = new StringBuilder();
        int sumPrize = 0;
        for (Member member : members) {
            if (member.isWinner()) {
                winners.append(member.getId()).append(",");
                sbPrize.append(member.getPrize()).append(",");
            }
            sumPrize += member.getPrize();
        }

        if (winners.length() > 1) {
            int cost = 0;
            try {
                cost = (int) Double.parseDouble(mParam2);;
            } catch (Exception e) {
            }
            if(sumPrize > (cost * members.size())) {
                Toast.makeText(getActivity(),"The sum of prize can't be greater than prize pool", Toast.LENGTH_LONG).show();
                return;
            }
            showProgress(true);
            winners.deleteCharAt(winners.length() - 1);
            sbPrize.deleteCharAt(sbPrize.length() - 1);
            String url = String.format(MANAGE_WINNER_URL, mParam1, winners.toString(), sbPrize.toString());
            StringRequest myGameRequest = new StringRequest(Request.Method.GET, url, onPayCompleteListener, onErrorListener);
            RequestManager.getInstance(getActivity()).getRequestQueue().add(myGameRequest);
        }
    }

    private Response.Listener<String> onPayCompleteListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Fragment manageGameFragment = ManagedGamesFragment.newInstance();
            FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, manageGameFragment)
                    .addToBackStack(null)
                    .commit();
            Toast.makeText(getActivity(), "Paid winner", Toast.LENGTH_LONG);
            Log.d("pay complete", response);
        }
    };
    List<Member> members = new ArrayList<>();
    private Response.Listener<String> onCompleteListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            double costPerWinner = Double.parseDouble(mParam2);
            if (Constants.Status.STATUS_OK.equals(baseResponse.getStatus())) {
                Golfnation golfApp = (Golfnation) getActivity().getApplication();
                MembersResponse membersResponse = gson.fromJson(response, MembersResponse.class);
                members = membersResponse.getDetails();
                int cost = 0;
                try {
                    cost = (int) costPerWinner;
                } catch (Exception e) {
                }
                ;
                if (members.size() > 0) {
                    tvCost.setText("$" + (members.size() * cost));
                } else {
                    tvCost.setText("$" + cost);
                }
                adapter.setData(members);
            } else {
                tvCost.setText("$" + costPerWinner);
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

    ProgressDialog pDialog;

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        if (show) {
            if (pDialog == null)
                pDialog = ProgressDialog.show(getActivity(), "", "Loading..", false);
            else
                pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
