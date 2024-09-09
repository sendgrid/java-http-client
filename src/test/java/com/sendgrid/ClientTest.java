package com.sendgrid;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

  private CloseableHttpClient mockHttpClient;
  private ClassicHttpResponse mockResponse;


  @Before
  public void setUp() {
    this.mockHttpClient = mock(CloseableHttpClient.class);
    this.mockResponse = mock(ClassicHttpResponse.class);
  }

  @Test
  public void testBuildUri() {
    Client client = new Client();
    String baseUri = "api.test.com";
    String endpoint = "/endpoint";
    URI uri = null;
    Map<String,String> queryParams = new HashMap<>();
    queryParams.put("test1", "1");
    queryParams.put("test2", "2");
    queryParams.put("test3", "3&4&5");
    try {
      uri = client.buildUri(baseUri, endpoint, queryParams);
    } catch (URISyntaxException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.fail(errors.toString());
    }

    URL url = null;
    try {
      url = uri.toURL();
    } catch (MalformedURLException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.fail(errors.toString());
    }

    Assert.assertEquals("https", url.getProtocol());
    Assert.assertEquals("api.test.com", url.getHost());
    Assert.assertEquals("/endpoint", url.getPath());
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
    BasicHeader[] mockedHeaders = null;
    try {
      when(mockResponse.getCode()).thenReturn(200);
      when(mockResponse.getEntity()).thenReturn(
              new InputStreamEntity(
                      new ByteArrayInputStream(
                              "{\"message\":\"success\"}".getBytes()), ContentType.APPLICATION_JSON));
      mockedHeaders = new BasicHeader[] { new BasicHeader("headerA", "valueA") };
      when(mockResponse.getHeaders()).thenReturn(mockedHeaders);
      when(mockHttpClient.execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class))).thenReturn(mockResponse);
      HttpGet httpGet = new HttpGet("https://api.test.com");
      ClassicHttpResponse resp = mockHttpClient.execute(httpGet, response -> response);
      testResponse = client.getResponse(resp);
      resp.close();
    } catch (IOException | HttpException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.fail(errors.toString());
    }

    Assert.assertEquals(200, testResponse.getStatusCode());
    Assert.assertEquals(testResponse.getBody(), "{\"message\":\"success\"}");
    Map<String,String> headers = new HashMap<>();
    for (BasicHeader h:mockedHeaders) {
      headers.put(h.getName(), h.getValue());
    }
    Assert.assertEquals(testResponse.getHeaders(), headers);
  }

  public void testMethod(Method method, int statusCode) {
    Response testResponse = new Response();
    Request request = new Request();
    BasicHeader[] mockedHeaders = null;
    try {
      when(mockResponse.getCode()).thenReturn(statusCode);
      when(mockResponse.getEntity()).thenReturn(
              new InputStreamEntity(
                      new ByteArrayInputStream(
                              "{\"message\":\"success\"}".getBytes()), ContentType.APPLICATION_JSON));
      mockedHeaders = new BasicHeader[] { new BasicHeader("headerA", "valueA") };
      when(mockResponse.getHeaders()).thenReturn(mockedHeaders);
      when(mockHttpClient.execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class))).thenReturn(mockResponse);
      request.setMethod(method);
      if ((method == Method.POST) || (method == Method.PATCH) || (method == Method.PUT)) {
        request.setBody("{\"test\":\"testResult\"}");
      }
      request.setEndpoint("/test");
      request.addHeader("Authorization", "Bearer XXXX");
      Client client = new Client(mockHttpClient);
      testResponse = client.get(request);
    } catch (URISyntaxException | IOException ex) {
      StringWriter errors = new StringWriter();
      ex.printStackTrace(new PrintWriter(errors));
      Assert.fail(errors.toString());
    }

    Assert.assertEquals(testResponse.getStatusCode(), statusCode);
    if (method != Method.DELETE) {
      Assert.assertEquals(testResponse.getBody(), "{\"message\":\"success\"}");
    }
    Assert.assertEquals(testResponse.getBody(), "{\"message\":\"success\"}");
    Map<String,String> headers = new HashMap<>();
    for (BasicHeader h:mockedHeaders) {
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
    return url.getQuery().contains(key + "=" + value);
  }
}
