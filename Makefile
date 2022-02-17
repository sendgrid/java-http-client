.PHONY: install package test update-deps test-integration clean

VERSION := $(shell mvn help:evaluate -Dexpression=project.version --batch-mode | grep -e '^[^\[]')
install:
	@java -version || (echo "Java is not installed, please install Java >= 7"; exit 1);
	mvn clean install -DskipTests=true -Dgpg.skip -B
	cp target/java-http-client-$(VERSION).jar java-http-client.jar

package:
	mvn package -DskipTests=true -Dgpg.skip -B
	cp target/java-http-client-$(VERSION).jar java-http-client.jar

test:
	mvn test

update-deps:
	mvn versions:use-latest-releases versions:commit -DallowMajorUpdates=false

clean:
	mvn clean
