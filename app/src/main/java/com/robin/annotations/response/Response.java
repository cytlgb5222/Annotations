package com.robin.annotations.response;

import java.io.Serializable;

/**
 * Created by robin on 2017/2/20.
 */

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
