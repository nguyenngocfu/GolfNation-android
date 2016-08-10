package com.golf.golfnation.game;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.golf.golfnation.Golfnation;
import com.golf.golfnation.MainActivity;
import com.golf.golfnation.R;
import com.golf.golfnation.common.Constants;
import com.golf.golfnation.common.RequestManager;
import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.game.model.Game;
import com.golf.golfnation.game.model.GameResponse;
import com.golf.golfnation.game.model.MyGame;
import com.golf.golfnation.user.model.LoginResponse;
import com.golf.golfnation.user.model.UserDetail;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesFragment extends Fragment implements Constants {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lvGames;
    private GameAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesFragment newInstance() {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public GamesFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_games, container, false);
        setHasOptionsMenu(true);
        lvGames = (ListView) rootView.findViewById(R.id.lv_games);
        adapter = new GameAdapter(getActivity());
        lvGames.setAdapter(adapter);
        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game game = (Game) adapter.getItem(position);
                UserDetail ud = ((Golfnation)getActivity().getApplication()).getUserDetail();
                if(game.getPassword().isEmpty() || game.getCreatorId().equals(ud.getUser_id())) {
                    Fragment detailFragment = GameDetailFragment.newInstance(game);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, detailFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    showPasswordDialog(game);
                }
            }
        });
        requestGames();
        return rootView;
    }

    protected void showPasswordDialog(final Game game) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialog_prompt_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.password);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().equals(game.getPassword())) {
                            Fragment detailFragment = GameDetailFragment.newInstance(game);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, detailFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    // Get game listing
    public void requestGames() {

        StringRequest myGameRequest = new StringRequest(Request.Method.GET, ALL_GAMES_URL, onCompleteListener, onErrorListener);
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
                Golfnation golfApp = (Golfnation) getActivity().getApplication();
                GameResponse gameResponse = gson.fromJson(response, GameResponse.class);

                adapter.setData(gameResponse.getDetails());
            } else {
                Toast.makeText(getActivity(), "There are no games available", Toast.LENGTH_LONG).show();
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
        if(show) {
            if(pDialog == null)
                pDialog = ProgressDialog.show(getActivity(), "","Loading..", false);
            else
                pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.game_listing, menu);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
            /*searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Search", "search la");
                }
            });*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()) searchGames(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Search", "search la1");
                return false;
            }
        });
    }

    // Get game listing
    public void searchGames(String keyword) {
        String searchURL = String.format(SEARCH_GAMES_URL, keyword);
        StringRequest myGameRequest = new StringRequest(Request.Method.GET, searchURL, onCompleteListener, onErrorListener);
        RequestManager.getInstance(getActivity()).getRequestQueue().add(myGameRequest);
        showProgress(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_search) {
            SearchManager searchManager =
                    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) item.getActionView();
            /*searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Search", "search la");
                }
            });*/
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("Search", "search la");
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("Search", "search la1");
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

}
