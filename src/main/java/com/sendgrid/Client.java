package com.sendgrid;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpMessage;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.net.URIBuilder;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.sun.jndi.toolkit.url.Uri;


/**
 * Class Client allows for quick and easy access any REST or REST-like API.
 */
public class Client implements Closeable {

	private final CloseableHttpClient httpClient;
	private final Boolean test;


	/**
	 * Constructor for using the default CloseableHttpClient.
	 */
	public Client() {
		this.httpClient = HttpClients.createDefault();
		this.test = false;
	}


	/**
	 * Constructor for passing in an httpClient, typically for mocking. Passed-in httpClient will not be closed
	 * by this Client.
	 *
	 * @param httpClient
	 *            an Apache CloseableHttpClient
	 */
	public Client(CloseableHttpClient httpClient) {
		this(httpClient, false);
	}


	/**
	 * Constructor for passing in a test parameter to allow for http calls.
	 *
	 * @param test
	 *            is a Bool
	 */
	public Client(Boolean test) {
		this(HttpClients.createDefault(), test);
	}


	/**
	 * Constructor for passing in an httpClient and test parameter to allow for http calls.
	 *
	 * @param httpClient
	 *            an Apache CloseableHttpClient
	 * @param test
	 *            is a Bool
	 */
	public Client(CloseableHttpClient httpClient, Boolean test) {
		this.httpClient = httpClient;
		this.test = test;
	}


	/**
	 * Add query parameters to a URL.
	 *
	 * @param baseUri
	 *            (e.g. "api.sendgrid.com")
	 * @param endpoint
	 *            (e.g. "/your/endpoint/path")
	 * @param queryParams
	 *            map of key, values representing the query parameters
	 * @throws URISyntaxException
	 *            in of a URI syntax error
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
			String multiValueDelimiter = "&";

			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				String value = entry.getValue();

				if (value.contains(multiValueDelimiter)) {
					String[] values = value.split(multiValueDelimiter);
					for (String val : values) {
						builder.addParameter(entry.getKey(), val);
					}
				} else {
					builder.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		return builder.build();
	}


	/**
	 * Prepare a Response object from an API call via Apache's HTTP client.
	 *
	 * @param response
	 *            from a call to a CloseableHttpClient
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	public Response getResponse(ClassicHttpResponse response) throws IOException, HttpException{
		HttpClientResponseHandler<String> handler = new SendGridResponseHandler();
		String responseBody = handler.handleResponse(response);

		int statusCode = response.getCode();

		Header[] headers = response.getHeaders();
		Map<String, String> responseHeaders = new HashMap<>();
		for (Header h : headers) {
			responseHeaders.put(h.getName(), h.getValue());
		}

		return new Response(statusCode, responseBody, responseHeaders);
	}


	/**
	 * Make a GET request and provide the status code, response body and
	 * response headers.
	 *
	 * @param request
	 *            the request object
	 * @throws URISyntaxException
	 *            in case of a URI syntax error
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	public Response get(Request request) throws URISyntaxException, IOException {
		URI uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
		HttpGet httpGet = new HttpGet(uri.toString());

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpGet.setHeader(entry.getKey(), entry.getValue());
			}
		}
		return executeApiCall(httpGet);
	}


	/**
	 * Make a POST request and provide the status code, response body and
	 * response headers.
	 *
	 * @param request
	 *            the request object
	 * @throws URISyntaxException
	 *            in case of a URI syntax error
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	public Response post(Request request) throws URISyntaxException, IOException {
		URI uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
		HttpPost httpPost = new HttpPost(uri.toString());

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpPost.setEntity(new StringEntity(request.getBody(), StandardCharsets.UTF_8));
		writeContentTypeIfNeeded(request, httpPost);

		return executeApiCall(httpPost);
	}


	/**
	 * Make a PATCH request and provide the status code, response body and
	 * response headers.
	 *
	 * @param request
	 *            the request object
	 * @throws URISyntaxException
	 *            in case of a URI syntax error
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	public Response patch(Request request) throws URISyntaxException, IOException {
		URI uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
		HttpPatch httpPatch = new HttpPatch(uri.toString());

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpPatch.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpPatch.setEntity(new StringEntity(request.getBody(), StandardCharsets.UTF_8));
		writeContentTypeIfNeeded(request, httpPatch);

		return executeApiCall(httpPatch);
	}


	/**
	 * Make a PUT request and provide the status code, response body and
	 * response headers.
	 *
	 * @param request
	 *            the request object
	 * @throws URISyntaxException
	 *            in case of a URI syntax error
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	public Response put(Request request) throws URISyntaxException, IOException {
		URI uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
		HttpPut httpPut = new HttpPut(uri.toString());

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpPut.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpPut.setEntity(new StringEntity(request.getBody(), StandardCharsets.UTF_8));
		writeContentTypeIfNeeded(request, httpPut);

		return executeApiCall(httpPut);
	}


	/**
	 * Make a DELETE request and provide the status code and response headers.
	 *
	 * @param request
	 *            the request object
	 * @throws URISyntaxException
	 *            in case of a URI syntax error
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	public Response delete(Request request) throws URISyntaxException, IOException {
		URI uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
		BasicClassicHttpRequest httpDelete = new BasicClassicHttpRequest("DELETE", uri.toString());

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpDelete.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpDelete.setEntity(new StringEntity(request.getBody(), StandardCharsets.UTF_8));
		writeContentTypeIfNeeded(request, httpDelete);

		return executeApiCall(httpDelete);
	}

	private void writeContentTypeIfNeeded(Request request, HttpMessage httpMessage) {
		if (!"".equals(request.getBody())) {
			httpMessage.setHeader("Content-Type", "application/json");
		}
	}


	/**
	 * Makes a call to the client API.
	 *
	 * @param httpPost
	 *            the request method object
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	private Response executeApiCall(BasicClassicHttpRequest httpPost) throws IOException {
		try {
			return getResponse(httpClient.execute(httpPost, response -> response));
		} catch(ClientProtocolException | HttpException e) {
			throw new IOException(e.getMessage());
		}
	}


	/**
	 * A thin wrapper around the HTTP methods.
	 *
	 * @param request
	 *            the request object
	 * @throws IOException
	 *            in case of a network error
	 * @return the response object
	 */
	public Response api(Request request) throws IOException {
		try {
			if (request.getMethod() == null) {
				throw new IOException("We only support GET, PUT, PATCH, POST and DELETE.");
			}
			switch (request.getMethod()) {
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
		}catch (URISyntaxException ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			throw new IOException(errors.toString());
		}
	}


	/**
	 * Closes the http client.
	 *
	 * @throws IOException
	 *            in case of a network error
	 */
	@Override
	public void close() throws IOException {
		this.httpClient.close();
	}

}
