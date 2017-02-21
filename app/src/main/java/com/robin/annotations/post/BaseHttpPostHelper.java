package com.robin.annotations.post;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robin.annotations.response.Response;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

/**
 * Created by robin on 2017/2/21.
 */

@EBean
public class BaseHttpPostHelper {

    @RootContext
    Context context;

    @UiThread
    protected void checkResponse(Response response, OnPostResponseListener listener, String action) {
        if (response != null) {
            if (response.isSuccess()) {
                if (null != listener) {
                    listener.onResponse(response.isSuccess(), response, action);
                }
            } else {
                listener.onResponse(false, response, action);
                if (!TextUtils.isEmpty(response.getError()))
                    Log.e("aaa", response.getError());

            }
        } else {
            listener.onResponse(false, response, action);
            Log.e("aaa", "网络异常");
        }
    }


    public interface OnPostResponseListener {
        public void onStartPost();

        public void onResponse(boolean status, Response response, String action);
    }
}
