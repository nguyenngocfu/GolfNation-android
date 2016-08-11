package com.golf.golfnation.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.golf.golfnation.game.model.Game;
import com.golf.golfnation.game.model.GameJoinResponse;
import com.golf.golfnation.game.model.GameResponse;
import com.golf.golfnation.user.model.UserDetail;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.s;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameDetailFragment extends Fragment implements Constants, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PAYPAL_BUTTON_ID = 99999999;
    private static final int REQUEST_CODE_PAY = 1111;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Game game;
    private AlertDialog alertDialog;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    // note that these credentials will differ between live & sandbox environments.
    //private static final String CONFIG_CLIENT_ID = "AS0uAR1G_uJRFxHiGdPT4G9tVVNqvTGHCOJVFHgvatMr-XlqbDLnaNjD6wZUkC_-85n4CTBBa9MWiHYS";
    private static final String CONFIG_CLIENT_ID = "Ae5i6fBikkLGb1Xqt1cu3OWi5Fmxz2LerFthPHGNG5eDS2nFru2sHJQmz7ThtTZq_cBiFFKUx1LR5bAJ";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GameDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameDetailFragment newInstance(Game game) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.GAME_DETAIL, game);
        fragment.setArguments(args);
        return fragment;
    }

    public GameDetailFragment() {
        // Required empty public constructor
    }

    Button btnJoin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        game = (Game) getArguments().getSerializable(Key.GAME_DETAIL);

        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
        checkGameJoinStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_detail, container, false);
        TextView tvName = (TextView) v.findViewById(R.id.golf_name);
        tvName.setText(game.getName());
        TextView tvInfo = (TextView) v.findViewById(R.id.tv_course);
        tvInfo.setText(game.getCourse());
        TextView tvCity = (TextView) v.findViewById(R.id.tv_city);
        tvCity.setText(game.getCity());
        TextView tvState = (TextView) v.findViewById(R.id.tv_state);
        tvState.setText(game.getState());
        SimpleDateFormat apiDateFormat = new SimpleDateFormat(Constants.Format.DATE_API_FORMAT);
        SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.Format.DATE_DISPLAY_FORMAT);
        SimpleDateFormat apiTimeFormat = new SimpleDateFormat(Constants.Format.TIME_API_FORMAT);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat(Constants.Format.TIME_DISPLAY_FORMAT);
        try {
            Date apiDate = apiDateFormat.parse(game.getDate());
            Date apiTime = apiTimeFormat.parse(game.getTime());

            TextView tvTime = (TextView) v.findViewById(R.id.tv_time);
            tvTime.setText(displayTimeFormat.format(apiTime));
            TextView tvDate = (TextView) v.findViewById(R.id.tv_date);
            tvDate.setText(displayFormat.format(apiDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView tvFormat = (TextView) v.findViewById(R.id.tv_format);
        tvFormat.setText(game.getType());
        TextView tvCost = (TextView) v.findViewById(R.id.tv_cost);
        int cost = 0;
        try {
            double dcost = Double.valueOf(game.getCost());
            cost = (int) dcost;
        } catch (Exception e) {};
        tvCost.setText("$"+cost);
        TextView tvPhone = (TextView) v.findViewById(R.id.tv_phone);
        tvPhone.setText(game.getPhone());
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + game.getPhone()));
                startActivity(callIntent );
            }
        });
        TextView tvDetail = (TextView) v.findViewById(R.id.tv_additional_detail);
        tvDetail.setText(game.getDetails());
        btnJoin = (Button) v.findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(this);
        setHasOptionsMenu(true);
        return v;
    }

    public void checkGameJoinStatus() {
        UserDetail userDetail = ((Golfnation) getActivity().getApplication()).getUserDetail();
        String url = String.format(JOIN_STATUS_URL, game.getId(), userDetail.getUser_id());
        showProgress(true);
        StringRequest joinGameRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showProgress(false);
                Gson gson = new Gson();
                GameJoinResponse baseResponse = gson.fromJson(response, GameJoinResponse.class);
                if (!"Already join".equalsIgnoreCase(baseResponse.getDetails())) {
                    btnJoin.setVisibility(View.VISIBLE);
                }
            }
        }, onErrorListener);
        RequestManager.getInstance(getActivity()).getRequestQueue().add(joinGameRequest);
        showProgress(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PAYMENT:
                if (resultCode == Activity.RESULT_OK) {
                    PaymentConfirmation confirm =
                            data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    String transactionID = confirm.getProofOfPayment().getTransactionId();
                    joinGame(game.getId(), transactionID);
                }
        }
    }

    public void joinGame(String gameId, String transactionID) {
        UserDetail userDetail = ((Golfnation) getActivity().getApplication()).getUserDetail();
        String url = String.format(JOIN_GAME_URL, gameId, userDetail.getUser_id(), transactionID);
        StringRequest joinGameRequest = new StringRequest(Request.Method.GET, url, onCompleteListener, onErrorListener);
        RequestManager.getInstance(getActivity()).getRequestQueue().add(joinGameRequest);
        showProgress(true);
    }

    private Response.Listener<String> onCompleteListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            if (Constants.Status.STATUS_OK.equals(baseResponse.getStatus())) {
                btnJoin.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "You have joined this game", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_join:
                btnJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder aldlgBuilder = new AlertDialog.Builder(getActivity());
                        aldlgBuilder.setMessage("Money transfer and fees are final. If you cannot make the game the moderator will redistribute funds at the end of the game to your account.")
                        .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //--- include an item list, payment amount details
                                double adminFee = Double.parseDouble(game.getCost()) * 0.09;
                                PayPalItem[] items =
                                        {
                                        /*new PayPalItem("game " + game.getName() + " ticket", 1, new BigDecimal(game.getCost()), "USD",
                                                game.getId()),*/

                                                new PayPalItem("9% for admin fee", 1, new BigDecimal(adminFee),
                                                        "USD", game.getId() + "-5")
                                        };
                                BigDecimal subtotal = PayPalItem.getItemTotal(items);
                                PayPalPayment thingToBuy = new PayPalPayment(subtotal, "USD", "Join game " + game.getName() + " fee", PayPalPayment.PAYMENT_INTENT_ORDER);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

                                Intent intent = new Intent(getActivity(), PaymentActivity.class);

                                // send the same configuration for restart resiliency
                                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();


                    }
                });
                break;
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, game.getName() + "\n" + game.getCourse() + " " + game.getCity());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share via"));
        }
        return super.onOptionsItemSelected(item);
    }
}