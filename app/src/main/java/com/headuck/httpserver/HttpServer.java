package com.headuck.httpserver;


import android.util.Log;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by wangmingxing on 2017/6/20.
 */

public class HttpServer extends NanoHTTPD {
    private static final String TAG = "HttpServer";

    public HttpServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String, String> header = session.getHeaders();
        Map<String, String> parms = session.getParms();

        Log.d(TAG, "uri=" + uri);
        Log.d(TAG, "method=" + method);
        Log.d(TAG, "header=" + header);
        Log.d(TAG, "params=" + parms);

        if (method.equals(Method.GET)) {
            // for file browse and download
            if ("/direct.pac".equals(uri)) {
                Log.d(TAG, "return pac file");
                String answer = "function FindProxyForURL(url, host)" +
                        "{ return \"DIRECT\"; }";
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/x-ns-proxy-autoconfig", answer);
            } else {
                Log.d(TAG, "not found: " + uri);
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain","Error! No such file or directory");
            }
        }

        return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, "text/plain","Request not supported");
    }


}
