package com.golf.golfnation.common;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ngocnq6508 on 6/25/2015.
 */
public class RequestManager {
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static RequestManager mInstance;
    private RequestManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestManager(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

}
