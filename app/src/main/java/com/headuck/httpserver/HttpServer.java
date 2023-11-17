import android.util.Log;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

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

                Response response = newFixedLengthResponse(Response.Status.OK, "application/x-ns-proxy-autoconfig", answer);

                // Add custom headers to the response
                for (Map.Entry<String, String> entry : getCustomHeaders().entrySet()) {
                    response.addHeader(entry.getKey(), entry.getValue());
                }

                return response;
            } else {
                Log.d(TAG, "not found: " + uri);
                return newFixedLengthResponse(Response.Status.OK, "text/plain", "Error! No such file or directory");
            }
        }

        return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, "text/plain", "Request not supported");
    }

    // Helper method to define custom headers
    private Map<String, String> getCustomHeaders() {
        // Define your custom headers here
        Map<String, String> customHeaders = Map.of(
                "Content-Security-Policy", "default-src 'self'; script-src 'self' *;img-src 'self' data:; style-src 'self' 'unsafe-inline';",
                "Content-Encoding", "gzip, deflate, brotli, compress, pack2, identity, zstd, windowbits, trailers, chunked",
                "Accept-Encoding", "gzip, deflate, brotli, compress, pack2, identity, zstd, windowbits, trailers, chunked",
                "Transfer-Encoding": "gzip, deflate, brotli, compress, pack2, identity, zstd, windowbits, trailers, chunked",
                "TE": "trailers, chunked",
                "X-TCP-CONGESTION-CONTROL": "bbr",
                "TCP_NODELAY": "1",
                "TCP_QUICKACK": "1",
                "Connection": "keep-alive, Upgrade",
                "Proxy-Connection": "keep-alive",
                "Range": "byte=-",
                "Upgrade": "websocket, xhr-streaming, QUIC, SSE, RTA/x11, IRC/6.9",
                "Cache-Control": "private max-age=0",
                "Preload": "65",
                "DNT": "1",
                "Trailer": "Max-Forwards",
                "SETTINGS": "SETTINGS_HEADER_TABLE_SIZE=8192, SETTINGS_MAX_CONCURRENT_STREAMS=200, SETTINGS_INITIAL_WINDOW_SIZE=131072",
                "X_UDPGW_ENABLE": "1",
                "Content-Range": "bytes 0-*",
                "Vary", "Accept-Encoding Accept-Language Cookie Range User-Agent"
        );

        return customHeaders;
    }
}
