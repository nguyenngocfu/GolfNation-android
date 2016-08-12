package com.golf.golfnation.user.controller;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.golf.golfnation.Golfnation;
import com.golf.golfnation.R;
import com.golf.golfnation.common.view.CircleTransform;
import com.golf.golfnation.user.model.UserDetail;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageView ivProfile;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyProfileFragment() {
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
    public void onResume() {
        super.onResume();
        UserDetail userDetail = ((Golfnation)getActivity().getApplication()).getUserDetail();
        Picasso.with(getActivity()).load(userDetail.getPhotoName()).resize(400,400).memoryPolicy(MemoryPolicy.NO_CACHE).transform(new CircleTransform()).into(ivProfile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        UserDetail userDetail = ((Golfnation)getActivity().getApplication()).getUserDetail();
        ivProfile = (ImageView) v.findViewById(R.id.profile_img);
        Picasso.with(getActivity()).load(userDetail.getPhotoName()).resize(400,400).memoryPolicy(MemoryPolicy.NO_CACHE).transform(new CircleTransform()).into(ivProfile);
        TextView tvName = (TextView) v.findViewById(R.id.name);
        tvName.setText(userDetail.getFirstname() + " " + userDetail.getLastname());
       /* TextView tvAddress = (TextView) v.findViewById(R.id.city);
        tvAddress.setText(userDetail.getLocation());*/
        TextView tvCourse = (TextView) v.findViewById(R.id.homecourse);
        SpannableStringBuilder sb = new SpannableStringBuilder("Home Course:");
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                0,sb.length(), 0);
        sb.append(" ");
        sb.append(userDetail.getHome_course());
        SpannableString homecourse = SpannableString.valueOf(sb);
        tvCourse.setText(homecourse);
        TextView tvHandicap = (TextView) v.findViewById(R.id.handicap);
        SpannableString handicap = new SpannableString("Handicap: "+userDetail.getHandicap());
        handicap.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                0,8, 0);
        tvHandicap.setText(handicap);
        Button btnEdit = (Button) v.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = UpdateProfileFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commitAllowingStateLoss();
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
