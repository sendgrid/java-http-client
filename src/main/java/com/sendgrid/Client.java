package com.sendgrid;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// Hack to get DELETE to accept a request body
@NotThreadSafe
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return Method.DELETE.name();
    }
}

/**
 * Class Client allows for quick and easy access any REST or REST-like API.
 */
public class Client {

    private CloseableHttpClient httpClient;
    private Boolean test;

    /**
     * Constructor for using the default CloseableHttpClient.
     */
    public Client() {
        this.httpClient = HttpClients.createDefault();
        this.test = false;
    }

    /**
     * Constructor for passing in an httpClient for mocking.
     *
     * @param httpClient an Apache CloseableHttpClient
     */
    public Client(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.test = false;
    }

    /**
     * Constructor for passing in a test parameter to allow for http calls
     *
     * @param test is a Bool
     */
    public Client(Boolean test) {
        this.httpClient = HttpClients.createDefault();
        this.test = test;
    }

    /**
     * Add query parameters to a URL.
     *
     * @param baseUri     (e.g. "api.sendgrid.com")
     * @param endpoint    (e.g. "/your/endpoint/path")
     * @param queryParams map of key, values representing the query parameters
     */
    public URI buildUri(String baseUri, String endpoint, Map<String, String> queryParams) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();

        if (this.test) {
            builder.setScheme("http");
        } else {
            builder.setScheme("https");
        }

        builder.setHost(baseUri);
        builder.setPath(endpoint);

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                builder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }

    /**
     * Prepare a Response object from an API call via Apache's HTTP client.
     *
     * @param response from a call to a CloseableHttpClient
     */
    public Response getResponse(CloseableHttpResponse response) throws IOException {
        ResponseHandler<String> handler = new SendGridResponseHandler();

        int statusCode = response.getStatusLine().getStatusCode();

        String responseBody = handler.handleResponse(response);

        Header[] headers = response.getAllHeaders();
        Map<String, String> responseHeaders = new HashMap<String, String>();
        for (Header h : headers) {
            responseHeaders.put(h.getName(), h.getValue());
        }

        return new Response(statusCode, responseBody, responseHeaders);
    }

    /**
     * Make a GET request and provide the status code, response body and
     * response headers.
     */
    public Response get(Request request) throws URISyntaxException, IOException {
        URI uri = buildUri(request.baseUri, request.endpoint, request.queryParams);
        HttpGet httpGet = new HttpGet(uri.toString());

        if (request.headers != null) {
            for (Map.Entry<String, String> entry : request.headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return executeApiCall(httpGet);
    }

    /**
     * Make a POST request and provide the status code, response body and
     * response headers.
     */
    public Response post(Request request) throws URISyntaxException, IOException {
        URI uri = buildUri(request.baseUri, request.endpoint, request.queryParams);
        HttpPost httpPost = new HttpPost(uri.toString());

        if (request.headers != null) {
            for (Map.Entry<String, String> entry : request.headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        httpPost.setEntity(new StringEntity(request.body, Charset.forName(StandardCharsets.UTF_8.toString())));
        if (!request.body.equals("")) {
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        }

        return executeApiCall(httpPost);
    }

    /**
     * Make a PATCH request and provide the status code, response body and
     * response headers.
     */
    public Response patch(Request request) throws URISyntaxException, IOException {
        URI uri = buildUri(request.baseUri, request.endpoint, request.queryParams);
        HttpPatch httpPatch = new HttpPatch(uri.toString());

        if (request.headers != null) {
            for (Map.Entry<String, String> entry : request.headers.entrySet()) {
                httpPatch.setHeader(entry.getKey(), entry.getValue());
            }
        }

        httpPatch.setEntity(new StringEntity(request.body, Charset.forName(StandardCharsets.UTF_8.toString())));
        if (!request.body.equals("")) {
            httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        }
        return executeApiCall(httpPatch);
    }

    /**
     * Make a PUT request and provide the status code, response body and
     * response headers.
     */
    public Response put(Request request) throws URISyntaxException, IOException {
        URI uri = buildUri(request.baseUri, request.endpoint, request.queryParams);
        HttpPut httpPut = new HttpPut(uri.toString());

        if (request.headers != null) {
            for (Map.Entry<String, String> entry : request.headers.entrySet()) {
                httpPut.setHeader(entry.getKey(), entry.getValue());
            }
        }

        httpPut.setEntity(new StringEntity(request.body, Charset.forName(StandardCharsets.UTF_8.toString())));
        if (!request.body.equals("")) {
            httpPut.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        }

        return executeApiCall(httpPut);
    }

    /**
     * Make a DELETE request and provide the status code and response headers.
     */
    public Response delete(Request request) throws URISyntaxException, IOException {
        URI uri = buildUri(request.baseUri, request.endpoint, request.queryParams);
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(uri.toString());

        if (request.headers != null) {
            for (Map.Entry<String, String> entry : request.headers.entrySet()) {
                httpDelete.setHeader(entry.getKey(), entry.getValue());
            }
        }

        httpDelete.setEntity(new StringEntity(request.body, Charset.forName(StandardCharsets.UTF_8.toString())));
        if (!request.body.equals("")) {
            httpDelete.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        }

        return executeApiCall(httpDelete);
    }

    private Response executeApiCall(HttpRequestBase httpPost) throws IOException {
        CloseableHttpResponse serverResponse = null;
        Response response = new Response();
        try {
            serverResponse = httpClient.execute(httpPost);
            response = getResponse(serverResponse);
            final StatusLine statusLine = serverResponse.getStatusLine();
            if (statusLine.getStatusCode() >= 300) {
                //throwing IOException here to not break API behavior.
                throw new IOException("Request returned status Code " + statusLine.getStatusCode() + "Body:" + (response != null ? response.body : null));
            }

        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
        return response;
    }

    /**
     * A thin wrapper around the HTTP methods.
     */
    public Response api(Request request) throws IOException {
        try {
            if (request.method == null) {
                throw new IOException("We only support GET, PUT, PATCH, POST and DELETE.");
            }
            switch (request.method) {
                case GET:
                    return get(request);
                case POST:
                    return post(request);
                case PUT:
                    return put(request);
                case PATCH:
                    return patch(request);
                case DELETE:
                    return delete(request);
                default:
                    throw new IOException("We only support GET, PUT, PATCH, POST and DELETE.");
            }
        } catch (URISyntaxException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            throw new IOException(errors.toString());
        }
    }
}