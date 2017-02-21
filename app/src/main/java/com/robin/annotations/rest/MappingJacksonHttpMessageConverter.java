package com.robin.annotations.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.robin.annotations.response.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Created by robin on 2017/2/20.
 */
public class MappingJacksonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    final static int BUFFER_SIZE = 4096;

    private Gson gson;

    private Type type = null;

    private boolean prefixJson = false;

    /**
     * Construct a new {@code GsonHttpMessageConverter} with a default
     * {@link Gson#Gson() Gson}.
     */
    public MappingJacksonHttpMessageConverter() {
        this(new Gson());
    }

    /**
     * Construct a new {@code GsonHttpMessageConverter}.
     *
     * @param serializeNulls
     *            true to generate json for null values
     */
    public MappingJacksonHttpMessageConverter(boolean serializeNulls) {
        this(serializeNulls ? new GsonBuilder().serializeNulls().create() : new Gson());
    }

    /**
     * Construct a new {@code GsonHttpMessageConverter}.
     *
     * @param gson
     *            a customized {@link Gson#Gson() Gson}
     */
    public MappingJacksonHttpMessageConverter(Gson gson) {
        super(new MediaType("application", "json", DEFAULT_CHARSET));
        setGson(gson);
    }

    /**
     * Sets the {@code Gson} for this view. If not set, a default
     * {@link Gson#Gson() Gson} is used.
     * <p>
     * Setting a custom-configured {@code Gson} is one way to take further
     * control of the JSON serialization process.
     *
     * @throws IllegalArgumentException
     *             if gson is null
     */
    public void setGson(Gson gson) {
        Assert.notNull(gson, "'gson' must not be null");
        this.gson = gson;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * Indicates whether the JSON output by this view should be prefixed with
     * "{} &&". Default is false.
     * <p>
     * Prefixing the JSON string in this manner is used to help prevent JSON
     * Hijacking. The prefix renders the string syntactically invalid as a
     * script so that it cannot be hijacked. This prefix does not affect the
     * evaluation of JSON, but if JSON validation is performed on the string,
     * the prefix would need to be ignored.
     */
    public void setPrefixJson(boolean prefixJson) {
        this.prefixJson = prefixJson;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return canWrite(mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        Reader json = new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
        try {
            String result = convertStreamToString(json).trim();
//			ManagerLog.i("解析json----------1", result);
            Log.i("aaa","result = "+result);
            JSONObject jsonObj = new JSONObject(result);
            result = jsonObj.toString();

            String data = "\"data\": \"\"";
            if (result.contains(data)) {
                result = result.replace(data, "\"data\":{}");
            }
            data = "\"data\":\"\"";
            if (result.contains(data)) {
                result = result.replace(data, "\"data\":{}");
            }
            // AbLogUtil.e("", "result="+result);
            // if(result.contains("ad_list")){
            // String adList = result.substring(result.indexOf("ad_list"),
            // result.length());
            // AbLogUtil.e("", "adList="+adList);
            // }
            Object obj = null;
            Type typeOfT = getType();
            if (typeOfT != null) {
                obj = new Gson().fromJson(result, typeOfT);
                if (obj != null) {
                    Response res = (Response) obj;
                }
                return obj;
            } else {
//                ManagerLog.i("解析json-------------2", result);
                obj = new Gson().fromJson(result, clazz);
                if (obj != null) {
                    Response res = (Response) obj;

                }
                return obj;
            }

        } catch (JsonSyntaxException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        } catch (JsonIOException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        } catch (JsonParseException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(),
                getCharset(outputMessage.getHeaders()));

        try {
            if (this.prefixJson) {
                writer.append("{} && ");
            }
            Type typeOfSrc = getType();
            if (typeOfSrc != null) {
                this.gson.toJson(o, typeOfSrc, writer);
            } else {
                this.gson.toJson(o, writer);
            }
            writer.close();
        } catch (JsonIOException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    // helpers

    private Charset getCharset(HttpHeaders headers) {
        if (headers != null && headers.getContentType() != null && headers.getContentType().getCharSet() != null) {
            return headers.getContentType().getCharSet();
        }
        return DEFAULT_CHARSET;
    }

    public String convertStreamToString(Reader json) {
        BufferedReader reader = new BufferedReader(json);
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                json.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
