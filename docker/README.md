# Supported tags and respective `Dockerfile` links
 - `v1.0.0`, `latest` [(Dockerfile)](https://github.com/sendgrid/java-http-client/blob/master/docker/Dockerfile)

# Quick reference
Due to Oracle's JDK license, you must build this Docker image using the official Oracle image located in the Docker Store. You will need a Docker store account. Once you have an account, you must accept the Oracle license [here](https://store.docker.com/images/oracle-serverjre-8). On the command line, type `docker login` and provide your credentials. You may then build the image using this command `docker build -t sendgrid/java-http-client -f Dockerfile .`

 - **Where to get help:**
   [Contact SendGrid Support](https://support.sendgrid.com/hc/en-us)

 - **Where to file issues:**
   https://github.com/sendgrid/java-http-client/issues

 - **Where to get more info:**
   [USAGE.md](https://github.com/sendgrid/java-http-client/blob/master/docker/USAGE.md)

 - **Maintained by:**
   [SendGrid Inc.](https://sendgrid.com)

# Usage examples
 - Most recent version: `docker run -it sendgrid/java-http-client`.
 - Your own fork:
   ```sh-session
   $ git clone https://github.com/you/cool-java-http-client.git
   $ realpath cool-java-http-client
   /path/to/cool-java-http-client
   $ docker run -it -v /path/to/cool-java-http-client:/mnt/java-http-client sendgrid/java-http-client
   ```

For more detailed information, see [USAGE.md](https://github.com/sendgrid/java-http-client/blob/master/docker/USAGE.md).

![SendGrid Logo](https://uiux.s3.amazonaws.com/2016-logos/email-logo%402x.png)
