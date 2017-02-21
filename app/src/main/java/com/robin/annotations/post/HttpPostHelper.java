package com.robin.annotations.post;

import com.robin.annotations.response.Response;
import com.robin.annotations.rest.RestServiceClient;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by robin on 2017/2/21.
 */

@EBean
public class HttpPostHelper extends BaseHttpPostHelper {
    @RestService
    RestServiceClient rest;

    @Background
    public void queryBProductsAll(OnPostResponseListener listener, String action) {
        if (null != listener) {
            listener.onStartPost();
        }
        Response response = null;
        try {
            response = rest.queryBProductsAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkResponse(response, listener, "queryBProductsAll");
        }
    }
}
