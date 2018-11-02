![SendGrid Logo](https://uiux.s3.amazonaws.com/2016-logos/email-logo%402x.png)

[![Build Status](https://travis-ci.org/sendgrid/java-http-client.svg?branch=master)](https://travis-ci.org/sendgrid/java-http-client)
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
* [Usage](#usage)
* [Roadmap](#roadmap)
* [How to Contribute](#contribute)
* [About](#about)
* [License](#license)

<a name="announcements"></a>
# Announcements
**NEW:** If you're a software engineer who is passionate about #DeveloperExperience and/or #OpenSource, [this is an incredible opportunity to join our #DX team](https://sendgrid.com/careers/role/1421152/?gh_jid=1421152) as a Developer Experience Engineer and work with [@thinkingserious](https://github.com/thinkingserious) and [@aroach](https://github.com/aroach)! Tell your friends :)

All updates to this project is documented in our [CHANGELOG](https://github.com/sendgrid/java-http-client/blob/master/CHANGELOG.md).

<a name="installation"></a>
# Installation

## Prerequisites

- Java version Oracle JDK 8 or OpenJDK 7

## Install via Maven w/ Gradle

```groovy
...
dependencies {
  ...
  compile 'com.sendgrid:java-http-client:4.2.0'
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
    <version>4.2.0</version>
</dependency>
```

`mvn install`

## Install via Fat Jar

[sendgrid-java-latest.jar](http://dx.sendgrid.com/downloads/java-http-client/java-http-client-latest.jar)

## Dependencies

- Please see the [build.gradle file](https://github.com/sendgrid/java-http-client/blob/master/build.gradle)

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

<a name="usage"></a>
# Usage

- [Example Code](https://github.com/sendgrid/java-http-client/tree/master/examples)
- [Library Usage Documentation](USAGE.md)


The example uses SendGrid, you can get your free account [here](https://sendgrid.com/free?source=java-http-client).

First, update your environment with your [SENDGRID_API_KEY](https://app.sendgrid.com/settings/api_keys).

```bash
echo "export SENDGRID_API_KEY='YOUR_API_KEY'" > sendgrid.env
echo "sendgrid.env" >> .gitignore
source ./sendgrid.env
```

```bash
mvn package
cd examples
javac -classpath {path_to}/sendgrid-java-http-client-4.0.0-jar.jar:. Example.java && java -classpath {path_to}/sendgrid-java-http-client-4.0.0-jar.jar:. Example
```

## Environment Variables 

You can do the following to create a .env file:

```cp .env_example .env```

Then, just add your API Key into your .env file.

<a name="roadmap"></a>
# Roadmap

If you are interested in the future direction of this project, please take a look at our [milestones](https://github.com/sendgrid/java-http-client/milestones). We would love to hear your feedback.

<a name="contribute"></a>
# How to Contribute

We encourage contribution to our projects please see our [CONTRIBUTING](https://github.com/sendgrid/java-http-client/blob/master/CONTRIBUTING.md) guide for details.

Quick links:

- [Feature Request](https://github.com/sendgrid/java-http-client/blob/master/CONTRIBUTING.md#feature-request)
- [Bug Reports](https://github.com/sendgrid/java-http-client/blob/master/CONTRIBUTING.md#submit-a-bug-report)
- [Sign the CLA to Create a Pull Request](https://github.com/sendgrid/java-http-client/blob/master/CONTRIBUTING.md#cla)
- [Improvements to the Codebase](https://github.com/sendgrid/java-http-client/blob/master/CONTRIBUTING.md#improvements-to-the-codebase)

<a name="about"></a>
# About

java-http-client is guided and supported by the SendGrid [Developer Experience Team](mailto:dx@sendgrid.com).

java-http-client is maintained and funded by SendGrid, Inc. The names and logos for java-http-client are trademarks of SendGrid, Inc.

# License
[The MIT License (MIT)](LICENSE.txt)
