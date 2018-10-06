package com.sendgrid;

import java.util.Map;

/**
  * Class Response provides a standard interface to an API's response.
  */
public class Response {
  private int statusCode;
  private String body;
  private Map<String, String> headers;

  /**
    * Set the API's response.
    */
  public Response(final int statusCode, final String responseBody, final Map<String, String> responseHeaders) {
    this.statusCode = statusCode;
    this.body = responseBody;
    this.headers = responseHeaders;
  }

  public Response() {
    this.reset();
  }

  /**
   * Place the object into an empty state.
   */
  public void reset() {
    this.statusCode = 0;
    this.body = "";
    this.headers = null;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String getBody() {
    return this.body;
  }

  public Map<String, String> getHeaders() {
    return this.headers;
  }

  public void setStatusCode(final int statusCode) {
    this.statusCode = statusCode;
  }

  public void setBody(final String body) {
    this.body = body;
  }

  public void setHeaders(final Map<String, String> headers) {
    this.headers = headers;
  }
}
