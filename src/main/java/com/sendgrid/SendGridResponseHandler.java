package com.sendgrid;

import java.io.IOException;

import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import java.nio.charset.StandardCharsets;

/**
 * A {@link org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler} that returns the response body as a
 * String
 * for all responses.
 * <p>
 * If this is used with
 * {@link org.apache.hc.client5.http.classic.HttpClient#execute(ClassicHttpRequest, HttpClientResponseHandler)}
 * HttpClient may handle redirects (3xx responses) internally.
 *
 * </p>
 *
 */
public class SendGridResponseHandler extends AbstractHttpClientResponseHandler<String>{

    /**
     * Read the entity from the response body and pass it to the entity handler
     * method if the response was successful (a 2xx status code). If no response
     * body exists, this returns null. If the response was unsuccessful (&gt;= 500
     * status code), throws an {@link IOException}.
     */
    @Override
    public String handleResponse(final ClassicHttpResponse response)
    throws IOException {
        final HttpEntity entity = response.getEntity();
        return entity == null ? null : handleEntity(entity);
    }

    @Override
    public String handleEntity(HttpEntity entity) throws IOException{
        try{
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }catch(ParseException e){
            throw new RuntimeException(e);
        }
    }
}
