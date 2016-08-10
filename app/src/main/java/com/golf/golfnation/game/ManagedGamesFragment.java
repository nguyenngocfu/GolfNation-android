package com.golf.golfnation.game;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.golf.golfnation.game.model.Game;
import com.golf.golfnation.game.model.GameResponse;
import com.golf.golfnation.user.model.UserDetail;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManagedGamesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManagedGamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagedGamesFragment extends Fragment implements  Constants{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView lvGames;
    private ManagedGameAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ManagedGamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagedGamesFragment newInstance() {
        ManagedGamesFragment fragment = new ManagedGamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ManagedGamesFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_managed_game, container, false);
        lvGames = (ListView) rootView.findViewById(R.id.lv_games);
        adapter = new ManagedGameAdapter(getActivity());
        lvGames.setAdapter(adapter);
        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment detailFragment = GameDetailFragment.newInstance((Game) adapter.getItem(position));
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        requestGames();

        return rootView;
    }

    // Get game listing
    public void requestGames() {
        UserDetail userDetail = ((Golfnation)getActivity().getApplication()).getUserDetail();
        String url = String.format(MANAGEDGAMES_URL, userDetail.getUser_id());

        StringRequest myGameRequest = new StringRequest(Request.Method.GET, url, onCompleteListener, onErrorListener);
        RequestManager.getInstance(getActivity()).getRequestQueue().add(myGameRequest);
        showProgress(true);
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

    private Response.Listener<String> onCompleteListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            if (Constants.Status.STATUS_OK.equals(baseResponse.getStatus())) {
                Golfnation golfApp = (Golfnation) getActivity().getApplication();
                GameResponse gameResponse = gson.fromJson(response, GameResponse.class);
                adapter.setData(gameResponse.getDetails());
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

    // TODO: Rename method, update argunt and hook method into UI event
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
