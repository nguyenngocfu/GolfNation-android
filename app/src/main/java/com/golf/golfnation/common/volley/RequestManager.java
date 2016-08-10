package com.golf.golfnation.common.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Manager for the queue
 * 
 * @author Trey Robinson
 * 
 */
public class RequestManager {

	/**
	 * the queue :-)
	 */
	private static RequestQueue mRequestQueue;

	/**
	 * Nothing to see here.
	 */
	private RequestManager() {
		// no instances
	}

	/**
	 * @param context
	 *            application context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley
				.newRequestQueue(context);
	}

	/**
	 * @return instance of the queue
	 *             if init has not yet been called
	 */
	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("Not initialized");
		}
	}

	public void cancelRequest(final String tag) {
		if (mRequestQueue != null)
			mRequestQueue.cancelAll(tag);
	}
}
