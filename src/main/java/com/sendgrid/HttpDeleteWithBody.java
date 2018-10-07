package com.sendgrid;

import java.net.URI;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

// Hack to get DELETE to accept a request body
@NotThreadSafe
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
  static final String METHOD_NAME = "DELETE";

  @Override
  public String getMethod() {
    return METHOD_NAME;
  }

  HttpDeleteWithBody(final String uri) {
    super();
    setURI(URI.create(uri));
  }
}
