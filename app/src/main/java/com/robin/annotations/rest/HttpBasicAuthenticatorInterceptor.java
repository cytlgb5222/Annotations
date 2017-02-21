package com.robin.annotations.rest;

import android.util.Log;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by robin on 2017/2/20.
 */

public class HttpBasicAuthenticatorInterceptor implements ClientHttpRequestInterceptor {
    final static int BUFFER_SIZE = 4096;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().setAcceptEncoding(Arrays.asList(ContentCodingType.GZIP, ContentCodingType.ALL));
        String params = new String(data);
        Log.i("aaa","Http request URL = "+request.getURI());
        ClientHttpResponse res = execution.execute(request, data);

        return res;
    }

}
