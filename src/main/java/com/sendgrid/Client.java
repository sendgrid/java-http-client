package com.sendgrid;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


/**
 * Hack to get DELETE to accept a request body.
 */
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
	public static final String METHOD_NAME = "DELETE";

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

	public HttpDeleteWithBody(final String uri) {
		super();
		setURI(URI.create(uri));
	}
}


/**
 * Class Client allows for quick and easy access any REST or REST-like API.
 */
public class Client implements Closeable {

	private CloseableHttpClient httpClient;
	private Boolean test;
	private boolean createdHttpClient;


	/**
	 * Constructor for using the default CloseableHttpClient.
	 */
	public Client() {
		this.httpClient = HttpClients.createDefault();
		this.test = false;
		this.createdHttpClient = true;
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
		this.createdHttpClient = true;
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
		URI uri = null;

		if (this.test == true) {
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

				if (value.indexOf(multiValueDelimiter) != -1) {
					List<String> values = Arrays.asList(value.split(multiValueDelimiter));
					for (String val : values) {
						builder.addParameter(entry.getKey(), val);
					}
				} else {
					builder.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}

		try {
			uri = builder.build();
		} catch (URISyntaxException ex) {
			throw ex;
		}

		return uri;
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
	public Response getResponse(CloseableHttpResponse response) throws IOException {
		ResponseHandler<String> handler = new SendGridResponseHandler();
		String responseBody = handler.handleResponse(response);

		int statusCode = response.getStatusLine().getStatusCode();

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
		URI uri = null;
		HttpGet httpGet = null;

		try {
			uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
			httpGet = new HttpGet(uri.toString());
		} catch (URISyntaxException ex) {
			throw ex;
		}

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
		URI uri = null;
		HttpPost httpPost = null;

		try {
			uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
			httpPost = new HttpPost(uri.toString());
		} catch (URISyntaxException ex) {
			throw ex;
		}

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpPost.setEntity(new StringEntity(request.getBody(), Charset.forName("UTF-8")));
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
		URI uri = null;
		HttpPatch httpPatch = null;

		try {
			uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
			httpPatch = new HttpPatch(uri.toString());
		} catch (URISyntaxException ex) {
			throw ex;
		}

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpPatch.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpPatch.setEntity(new StringEntity(request.getBody(), Charset.forName("UTF-8")));
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
		URI uri = null;
		HttpPut httpPut = null;

		try {
			uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
			httpPut = new HttpPut(uri.toString());
		} catch (URISyntaxException ex) {
			throw ex;
		}

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpPut.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpPut.setEntity(new StringEntity(request.getBody(), Charset.forName("UTF-8")));
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
		URI uri = null;
		HttpDeleteWithBody httpDelete = null;

		try {
			uri = buildUri(request.getBaseUri(), request.getEndpoint(), request.getQueryParams());
			httpDelete = new HttpDeleteWithBody(uri.toString());
		} catch (URISyntaxException ex) {
			throw ex;
		}

		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				httpDelete.setHeader(entry.getKey(), entry.getValue());
			}
		}

		httpDelete.setEntity(new StringEntity(request.getBody(), Charset.forName("UTF-8")));
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
	private Response executeApiCall(HttpRequestBase httpPost) throws IOException {
		try {
			CloseableHttpResponse serverResponse = httpClient.execute(httpPost);
			try {
				return getResponse(serverResponse);
			} finally {
				serverResponse.close();
			}
		} catch(ClientProtocolException e) {
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
		} catch (IOException ex) {
			throw ex;
		} catch (URISyntaxException ex) {
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


	/**
	 * Closes and finalizes the http client.
	 *
	 * @throws Throwable
	 *            in case of an error
	 */		
	@Override
	public void finalize() throws Throwable {
		try {
			close();
		} catch(IOException e) {
			throw new Throwable(e.getMessage());
		} finally {
			super.finalize();
		}
	}
}
