# Usage
- [SendGrid Docs](https://sendgrid.com/docs/API_Reference/SMTP_API/index.html)
- [Example Code](https://github.com/sendgrid/java-http-client/tree/master/examples)

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