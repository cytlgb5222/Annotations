package com.robin.annotations;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.robin.annotations.post.BaseHttpPostHelper;
import com.robin.annotations.post.HttpPostHelper;
import com.robin.annotations.response.Response;
import com.robin.annotations.rest.RestServiceClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

//@Fullscreen
@EActivity(R.layout.activity_main)
//@WindowFeature(Window.FEATURE_NO_TITLE)
public class MainActivity extends AppCompatActivity implements BaseHttpPostHelper.OnPostResponseListener {

    @ViewById
    TextView tv_hello;

    @RestService
    RestServiceClient rest;

    @Bean
    HttpPostHelper httpPostHelper;

    @AfterViews
    void afterView(){

    }

    @Click(R.id.tv_hello)
    void helloClicked(){
//        helloBack();  // 方法一 请求
        helloBackPost();// 方法二 请求
    }

    private void helloBackPost() {
        httpPostHelper.queryBProductsAll(this,"queryBProductsAll");
    }

    @Background
     void helloBack() {
        Response response = rest.queryBProductsAll();
        queryBProductsAll(response);
    }

    @UiThread
    void queryBProductsAll(Response response) {

    }


    @Override
    public void onStartPost() {

    }

    @Override
    public void onResponse(boolean status, Response response, String action) {
        if (status){
            if (TextUtils.equals(action,"queryBProductsAll")){
                showData(response);
            }
        }
    }

    private void showData(Response response) {

    }
}
