![SendGrid Logo](twilio_sendgrid_logo.png)

[![Build Status](https://travis-ci.org/sendgrid/java-http-client.svg?branch=main)](https://travis-ci.org/sendgrid/java-http-client)
[![Maven Central](https://img.shields.io/maven-central/v/com.sendgrid/java-http-client.svg)](http://mvnrepository.com/artifact/com.sendgrid/java-http-client)
[![MIT licensed](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE.md)
[![Twitter Follow](https://img.shields.io/twitter/follow/sendgrid.svg?style=social&label=Follow)](https://twitter.com/sendgrid)
[![GitHub contributors](https://img.shields.io/github/contributors/sendgrid/java-http-client.svg)](https://github.com/sendgrid/java-http-client/graphs/contributors)

**Quickly and easily access any RESTful or RESTful-like API.**

If you are looking for the SendGrid API client library, please see [this repo](https://github.com/sendgrid/sendgrid-java).

# Table of Contents

* [Announcements](#announcements)
* [Installation](#installation)
* [Quick Start](#quick-start)
* [Usage](./USAGE.md)
* [How to Contribute](#contribute)
* [About](#about)
* [License](#license)

<a name="announcements"></a>
# Announcements
**The default branch name for this repository has been changed to `main` as of 07/27/2020.**

All updates to this project are documented in our [CHANGELOG](CHANGELOG.md).

<a name="installation"></a>
# Installation

## Prerequisites

- Java 8 or 11

## Install via Maven w/ Gradle

```groovy
...
dependencies {
  ...
  compile 'com.sendgrid:java-http-client:4.3.6'
}

repositories {
  mavenCentral()
}
...
```

### Maven

```xml
<dependency>
    <groupId>com.sendgrid</groupId>
    <artifactId>java-http-client</artifactId>
    <version>4.3.6</version>
</dependency>
```

`mvn install`

## Install via Fat Jar

[sendgrid-java-latest.jar](http://dx.sendgrid.com/downloads/java-http-client/java-http-client-latest.jar)

## Dependencies

- Please see the [pom.xml file](pom.xml)

<a name="quick-start"></a>
# Quick Start

Here is a quick example:

`GET /your/api/{param}/call`

```java
Client client = new Client();

Request request = new Request();
request.setBaseUri("api.test.com");
request.setMethod(Method.GET);
String param = "param";
request.setEndpoint("/your/api/" + param + "/call");

try {
    Response response = client.api(request);
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody());
    System.out.println(response.getHeaders());
} catch (IOException ex) {
    throw ex;
}
```

`POST /your/api/{param}/call` with headers, query parameters and a request body.

```java
request.addHeader("Authorization", "Bearer YOUR_API_KEY");
request.addQueryParam("limit", "100");
request.addQueryParam("offset", "0");
request.setBody("{\"name\": \"My Request Body\"}");
request.setMethod(Method.POST);
String param = "param";
request.setEndpoint("/your/api/" + param + "/call");

try {
    Response response = client.api(request);
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody());
    System.out.println(response.getHeaders());
} catch (IOException ex) {
    throw ex;
}
```

<a name="contribute"></a>
# How to Contribute

We encourage contribution to our projects please see our [CONTRIBUTING](CONTRIBUTING.md) guide for details.

Quick links:

- [Feature Request](CONTRIBUTING.md#feature-request)
- [Bug Reports](CONTRIBUTING.md#submit-a-bug-report)
- [Improvements to the Codebase](CONTRIBUTING.md#improvements-to-the-codebase)
- [Review Pull Requests](CONTRIBUTING.md#Code-Reviews)

<a name="about"></a>
# About

java-http-client is maintained and funded by Twilio SendGrid, Inc. The names and logos for java-http-client are trademarks of Twilio SendGrid, Inc.

If you need help installing or using the library, please check the [Twilio SendGrid Support Help Center](https://support.sendgrid.com).

If you've instead found a bug in the library or would like new features added, go ahead and open issues or pull requests against this repo!

# License
[The MIT License (MIT)](LICENSE.md)
