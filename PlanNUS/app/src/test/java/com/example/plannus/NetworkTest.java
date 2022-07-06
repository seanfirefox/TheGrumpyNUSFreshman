package com.example.plannus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.plannus.Objects.TimetableSettings;
import com.example.plannus.utils.RequestBuilder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkTest {

    private final String URL = "https://plannus-sat-solver.herokuapp.com/z3runner";
    private final String MAINURL ="https://plannus-sat-solver.herokuapp.com/";
    private final String failURL = "https://plannus-sat-solver.herokuapp.com/fail";

    @Test
    public void TestStatusCode_MAIN() throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                        .url(MAINURL)
                        .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        assertEquals(200, response.code());
    }

    @Test
    public void TestStatusCode_FAIL() throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(failURL)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        assertEquals(404, response.code());
    }

    @Test
    public void POST_REQUEST_TEST() throws Exception {
        HashMap<String, Boolean> constraints = new HashMap<>();
        constraints.put("no8amLessons", false);
        constraints.put("oneFreeDay", false);
        TimetableSettings ts = new TimetableSettings(new ArrayList<String>(Arrays.asList("CS2030S", "CS2040S", "CS2109S")),
                constraints,
                "2021-2022",
                "2"
                );

        RequestBody requestBody = new RequestBuilder(ts, "testing12345", URL)
                .buildRequestBody(0);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        assertEquals(200, response.code());
        assertNotEquals("Heroku site", response.body().string());

    }

    @Test
    public void GET_REQUEST_TEST() throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(MAINURL)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        assertEquals(200, response.code());
        assertEquals("Heroku site", response.body().string());
    }

}
