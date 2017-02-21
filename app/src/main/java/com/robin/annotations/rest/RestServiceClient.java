package com.robin.annotations.rest;

import com.robin.annotations.response.Response;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;

/**
 * Created by robin on 2017/2/20.
 */

//@Rest("http://192.168.0.13:8080/ws-web/bUsers/findTicList")
@Rest(rootUrl = "http://192.168.0.13:8080/ws-web/",converters = { MappingJacksonHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
public interface RestServiceClient extends RestClientErrorHandling{

    @Get("queryBProductsAll")
    @Accept(MediaType.APPLICATION_JSON)
    Response queryBProductsAll();

}
