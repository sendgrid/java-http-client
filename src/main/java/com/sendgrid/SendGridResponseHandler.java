package com.sendgrid;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;
import java.nio.charset.StandardCharsets;

/**
 * A {@link org.apache.http.client.ResponseHandler} that returns the response body as a String
 * for all responses. 
 * <p>
 * If this is used with
 * {@link org.apache.http.client.HttpClient#execute(
 *  org.apache.http.client.methods.HttpUriRequest, org.apache.http.client.ResponseHandler)},
 * HttpClient may handle redirects (3xx responses) internally.
 * </p>
 *
 */
public class SendGridResponseHandler extends AbstractResponseHandler<String>{

    /**
     * Read the entity from the response body and pass it to the entity handler
     * method if the response was successful (a 2xx status code). If no response
     * body exists, this returns null. If the response was unsuccessful (&gt;= 500
     * status code), throws an {@link HttpResponseException}.
     */
    @Override
    public String handleResponse(final HttpResponse response)
            throws HttpResponseException, IOException {
        final HttpEntity entity = response.getEntity();
        return entity == null ? null : handleEntity(entity);
    }
	
    @Override
    public String handleEntity(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }
}
