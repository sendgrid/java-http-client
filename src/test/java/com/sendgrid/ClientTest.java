package com.sendgrid;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ClientTest extends Mockito {

  private CloseableHttpClient httpClient;
  private CloseableHttpResponse response;
  private HttpEntity entity;
  private StatusLine statusline;

  @Before
  public void setUp() throws Exception {
    this.httpClient = mock(CloseableHttpClient.class);
    this.response = mock(CloseableHttpResponse.class);
    this.entity = mock(HttpEntity.class);
    this.statusline = mock(StatusLine.class);
  }

  @Test
  public void testbuildUri() {
    Client client = new Client();
    String baseUri = "api.test.com";
    String endpoint = "/endpoint";
    URI uri = null;
    Map<String,String> queryParams = new HashMap<String,String>();
    queryParams.put("test1", "1");
    queryParams.put("test2", "2");
    queryParams.put("test3", "3&4&5");
    try {
      uri = client.buildUri(baseUri, endpoint, queryParams);
    } catch (URISyntaxException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.assertTrue(errors.toString(), false);
    }

    URL url = null;
    try {
      url = uri.toURL();
    } catch (MalformedURLException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.assertTrue(errors.toString(), false);
    }

    Assert.assertTrue(url.getProtocol().equals("https"));
    Assert.assertTrue(url.getHost().equals("api.test.com"));
    Assert.assertTrue(url.getPath().equals("/endpoint"));
    Assert.assertTrue(this.queryParamHasCorrectValue(url, "test1", "1"));
    Assert.assertTrue(this.queryParamHasCorrectValue(url, "test2", "2"));
    Assert.assertTrue(this.queryParamHasCorrectValue(url, "test3", "3"));
    Assert.assertTrue(this.queryParamHasCorrectValue(url, "test3", "4"));
    Assert.assertTrue(this.queryParamHasCorrectValue(url, "test3", "5"));
  }

  @Test
  public void testGetResponse() {
    Client client = new Client();
    Response testResponse = new Response();
    Header[] mockedHeaders = null;
    try {
      when(statusline.getStatusCode()).thenReturn(200);
      when(response.getStatusLine()).thenReturn(statusline);
      when(response.getEntity()).thenReturn(
          new InputStreamEntity(
            new ByteArrayInputStream(
              "{\"message\":\"success\"}".getBytes())));
      mockedHeaders = new Header[] { new BasicHeader("headerA", "valueA") };
      when(response.getAllHeaders()).thenReturn(mockedHeaders);
      when(httpClient.execute(Matchers.any(HttpGet.class))).thenReturn(response);
      HttpGet httpGet = new HttpGet("https://api.test.com");
      CloseableHttpResponse resp = httpClient.execute(httpGet);
      testResponse = client.getResponse(resp);
      resp.close();
    } catch (IOException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.assertTrue(errors.toString(), false);
    }

    Assert.assertTrue(testResponse.getStatusCode() == 200);
    Assert.assertEquals(testResponse.getBody(), "{\"message\":\"success\"}");
    Map<String,String> headers = new HashMap<String,String>();
    for (Header h:mockedHeaders) {
      headers.put(h.getName(), h.getValue());
    }
    Assert.assertEquals(testResponse.getHeaders(), headers);
  }

  public void testMethod(Method method, int statusCode) {
    Response testResponse = new Response();
    Request request = new Request();
    Header[] mockedHeaders = null;
    try {
      when(statusline.getStatusCode()).thenReturn(statusCode);
      when(response.getStatusLine()).thenReturn(statusline);
      when(response.getEntity()).thenReturn(
          new InputStreamEntity(
            new ByteArrayInputStream(
              "{\"message\":\"success\"}".getBytes())));
      mockedHeaders = new Header[] { new BasicHeader("headerA", "valueA") };
      when(response.getAllHeaders()).thenReturn(mockedHeaders);
      when(httpClient.execute(Matchers.any(HttpGet.class))).thenReturn(response);
      request.setMethod(method);
      if ((method == Method.POST) || (method == Method.PATCH) || (method == Method.PUT)) {
        request.setBody("{\"test\":\"testResult\"}");
      }
      request.setEndpoint("/test");
      request.addHeader("Authorization", "Bearer XXXX");
      Client client = new Client(httpClient);
      testResponse = client.get(request);
    } catch (URISyntaxException | IOException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.assertTrue(errors.toString(), false);
    }

    Assert.assertTrue(testResponse.getStatusCode() == statusCode);
    if (method != Method.DELETE) {
      Assert.assertEquals(testResponse.getBody(), "{\"message\":\"success\"}");
    }
    Assert.assertEquals(testResponse.getBody(), "{\"message\":\"success\"}");
    Map<String,String> headers = new HashMap<String,String>();
    for (Header h:mockedHeaders) {
      headers.put(h.getName(), h.getValue());
    }
    Assert.assertEquals(testResponse.getHeaders(), headers);
  }

  @Test
  public void testGet() {
    testMethod(Method.GET, 200);
  }

  @Test
  public void testPost() {
    testMethod(Method.POST, 201);
  }

  @Test
  public void testPatch() {
    testMethod(Method.PATCH, 200);
  }

  @Test
  public void testPut() {
    testMethod(Method.PUT, 200);
  }

  @Test
  public void testDelete() {
    testMethod(Method.DELETE, 204);
  }

  private boolean queryParamHasCorrectValue(URL url, String key, String value) {
    return url.getQuery().indexOf(key + "=" + value) != -1;
  }
}
