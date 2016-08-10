package com.golf.golfnation.game;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.golf.golfnation.Golfnation;
import com.golf.golfnation.R;
import com.golf.golfnation.common.Constants;
import com.golf.golfnation.common.RequestManager;
import com.golf.golfnation.common.Utils;
import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.game.model.City;
import com.golf.golfnation.game.model.CityResponse;
import com.golf.golfnation.game.model.GameCourse;
import com.golf.golfnation.game.model.GameCourseResponse;
import com.golf.golfnation.game.model.GameType;
import com.golf.golfnation.game.model.GameTypeResponse;
import com.golf.golfnation.game.model.State;
import com.golf.golfnation.game.model.StateResponse;
import com.golf.golfnation.user.model.UserDetail;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGameFragment extends Fragment implements Constants{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etTitle,  etCost, etAdditionDetail, etPhone, etPassword, etPlayerNumber;
    Button btnDate, btnTime;
    Spinner spnCourse, spnType, spnState, spnCity;
    Button btnGo;
    List<GameCourse> gameCourses;
    List<State> stateList;
    List<City> cityList;
    List<GameType> gameTypes;
    private String seletedDate;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGameFragment newInstance() {
        CreateGameFragment fragment = new CreateGameFragment();
        return fragment;
    }

    public CreateGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        requestStateAndType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_game, container, false);

        etTitle = (EditText)v.findViewById(R.id.tv_title);
        etPhone = (EditText)v.findViewById(R.id.et_phone);
        etPassword = (EditText)v.findViewById(R.id.et_password);
        etPlayerNumber = (EditText) v.findViewById(R.id.et_limit_players);
        spnState = (Spinner)v.findViewById(R.id.spn_state);
        spnCity = (Spinner)v.findViewById(R.id.spn_city);
        spnCourse = (Spinner)v.findViewById(R.id.spn_course);
        btnDate = (Button)v.findViewById(R.id.tv_date);
        btnTime = (Button)v.findViewById(R.id.tv_time);
        // Open datetime picker whenever touch to edit text date & time
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.Format.DATE_DISPLAY_FORMAT);
        final SimpleDateFormat apiFormat = new SimpleDateFormat(Constants.Format.DATE_API_FORMAT);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                btnDate.setText(displayFormat.format(calendar.getTime()));
                                seletedDate = apiFormat.format(calendar.getTime());
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
                datePickerDialog.show();
            }
        });
        btnDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                btnDate.setText(displayFormat.format(calendar.getTime()));
                                seletedDate = apiFormat.format(calendar.getTime());
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
                if (focused) {
                    datePickerDialog.show();
                } else {
                    datePickerDialog.dismiss();
                }
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog datePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minutes);
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaaa");
                                btnTime.setText(sdf.format(calendar.getTime()));
                            }
                        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                datePickerDialog.show();
            }
        });
        btnTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                TimePickerDialog datePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minutes);
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaaa");
                                btnTime.setText(sdf.format(calendar.getTime()));
                            }
                        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                if (focused) {
                    datePickerDialog.show();
                } else {
                    datePickerDialog.dismiss();
                }
            }
        });
        spnType = (Spinner)v.findViewById(R.id.spn_type);
        etCost = (EditText)v.findViewById(R.id.tv_cost);
        etAdditionDetail = (EditText) v.findViewById(R.id.tv_additional_detail);

        spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                requestCityList(stateList.get(position - 1).getStateName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                    try {
                        requestCourseList(cityList.get(position - 1).getCityName(), stateList.get(spnState.getSelectedItemPosition() - 1).getStateName());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGo = (Button) (Button) v.findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requestCreateGames();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    // Retrieve list of course and type from server
    public void requestStateAndType() {
        StringRequest courseRequest = new StringRequest(Request.Method.GET, STATE_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            StateResponse stateResponse = gson.fromJson(s, StateResponse.class);
                            stateList = stateResponse.getDetails();
                            List<String> stateNames = new ArrayList<>();
                            stateNames.add("Select State");
                            if(stateList != null) {
                                for (State state: stateList) {
                                    stateNames.add(state.getStateName());
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, stateNames);
                            spnState.setAdapter(adapter);
                            List<String> cities = new ArrayList<>();
                            cities.add("Select City");
                            ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, cities);
                            spnCity.setAdapter(cityAdapter);
                            List<String> courses = new ArrayList<>();
                            courses.add("Select Golf Course");
                            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, courses);
                            spnCourse.setAdapter(courseAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        StringRequest typeRequest = new StringRequest(Request.Method.GET, TYPE_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            GameTypeResponse gameTypeResponse = gson.fromJson(s, GameTypeResponse.class);
                            gameTypes = gameTypeResponse.getDetails();
                            List<String> gameTypeNames = new ArrayList<>();
                            gameTypeNames.add("Select Game Type");
                            if(gameTypes != null) {
                                for (GameType type: gameTypes) {
                                    gameTypeNames.add(type.getTypeName());
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, gameTypeNames);
                            spnType.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        RequestManager.getInstance(getActivity()).getRequestQueue().add(courseRequest);
        RequestManager.getInstance(getActivity()).getRequestQueue().add(typeRequest);
    }

    public void requestCityList(String stateName) {
        String citiesURL = String.format(CITY_LIST_URL, stateName);
        StringRequest courseRequest = new StringRequest(Request.Method.GET, citiesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            CityResponse cityResponse = gson.fromJson(s, CityResponse.class);
                            cityList = cityResponse.getDetails();
                            List<String> cityNames = new ArrayList<>();
                            cityNames.add("Select City");
                            if(cityList != null) {
                                for (City city: cityList) {
                                    cityNames.add(city.getCityName());
                                }
                            }
                            ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, cityNames);
                            spnCity.setAdapter(cityAdapter);
                            List<String> courses = new ArrayList<>();
                            courses.add("Select Golf Course");
                            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, courses);
                            spnCourse.setAdapter(courseAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        RequestManager.getInstance(getActivity()).getRequestQueue().add(courseRequest);
    }

    public void requestCourseList(String cityName, String stateName) throws MalformedURLException, URISyntaxException {
        String courseURL = String.format(COURSE_LIST_URL, cityName, stateName);
        URL url = new URL(courseURL);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        StringRequest courseRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            GameCourseResponse gameCourseResponse = gson.fromJson(s, GameCourseResponse.class);
                            gameCourses = gameCourseResponse.getDetails();
                            List<String> coursenames = new ArrayList<>();
                            coursenames.add("Select Course");
                            if(gameCourses != null) {
                                for (GameCourse course: gameCourses) {
                                    coursenames.add(course.getCourseName());
                                }
                            }
                            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, coursenames);
                            spnCourse.setAdapter(courseAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        RequestManager.getInstance(getActivity()).getRequestQueue().add(courseRequest);
    }

    // Get game listing
    public void requestCreateGames() throws MalformedURLException, URISyntaxException {
        // Validate input
        if(etTitle.getText().length() == 0) {
            etTitle.setError("This field is required");
            return;
        }
        if((spnCourse.getSelectedItem() == null) ) {
            Toast.makeText(getActivity(), "There is no course matching with this filter", Toast.LENGTH_LONG).show();
            return;
        }
        if(spnCourse.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please select game course", Toast.LENGTH_LONG).show();
            return;
        }
        if((spnType.getSelectedItem() == null) ) {
            Toast.makeText(getActivity(), "There is no type matching with this filter", Toast.LENGTH_LONG).show();
            return;
        }
        if(spnType.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please select game type", Toast.LENGTH_LONG).show();
            return;
        }
        if(btnDate.getText().length() == 0) {
            btnDate.setError("This field is required");
            return;
        }
        if(btnTime.getText().length() == 0) {
            btnTime.setError("This field is required");
            return;
        }
        if(etCost.getText().length() == 0) {
            etCost.setError("This field is required");
            return;
        }
        if(!Utils.isNumeric(etCost.getText().toString())) {
            etCost.setError("Cost must be numeric type");
            return;
        }
        if((etPlayerNumber.getText().toString().length() > 0) && !Utils.isNumeric(etPlayerNumber.getText().toString())) {
            etPlayerNumber.setError("Maximum player number must be numeric type");
            return;
        }
        UserDetail userDetail = ((Golfnation)getActivity().getApplication()).getUserDetail();
        String createGameURL = String.format(CREATE_GAME_URL, userDetail.getUser_id(), etTitle.getText().toString(),
                gameCourses.get(spnCourse.getSelectedItemPosition() - 1).getCourseId(), gameTypes.get(spnType.getSelectedItemPosition() - 1).getTypeId(), etCost.getText().toString(),
                seletedDate, btnTime.getText().toString(), etAdditionDetail.getText().toString(), etPhone.getText().toString(), etPassword.getText().toString(), etPlayerNumber.getText().toString());
        URL url = new URL(createGameURL);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        StringRequest myGameRequest = new StringRequest(Request.Method.GET, url.toString(), onCompleteListener, onErrorListener);
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
                Toast.makeText(getActivity(), baseResponse.getMessage(), Toast.LENGTH_LONG).show();
                Fragment fragment = ManagedGamesFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commitAllowingStateLoss();
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
/*        try {
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
