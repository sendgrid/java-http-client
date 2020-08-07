Hello! Thank you for choosing to help contribute to one of the SendGrid open source projects. There are many ways you can contribute and help is always welcome.  We simply ask that you follow the following contribution policies.

- [Feature Request](#feature-request)
- [Submit a Bug Report](#submit-a-bug-report)
- [Improvements to the Codebase](#improvements-to-the-codebase)
- [Understanding the Code Base](#understanding-the-codebase)
- [Testing](#testing)
- [Style Guidelines & Naming Conventions](#style-guidelines-and-naming-conventions)
- [Creating a Pull Request](#creating-a-pull-request)
- [Code Reviews](#code-reviews)

We use [Milestones](https://github.com/sendgrid/java-http-client/milestones) to help define current roadmaps, please feel free to grab an issue from the current milestone. Please indicate that you have begun work on it to avoid collisions. Once a PR is made, community review, comments, suggestions and additional PRs are welcomed and encouraged.

<a name="feature-request"></a>
## Feature Request

If you'd like to make a feature request, please read this section.

The GitHub issue tracker is the preferred channel for library feature requests, but please respect the following restrictions:

- Please **search for existing issues** in order to ensure we don't have duplicate bugs/feature requests.
- Please be respectful and considerate of others when commenting on issues

<a name="submit-a-bug-report"></a>
## Submit a Bug Report

Note: DO NOT include your credentials in ANY code examples, descriptions, or media you make public.

A software bug is a demonstrable issue in the code base. In order for us to diagnose the issue and respond as quickly as possible, please add as much detail as possible into your bug report.

Before you decide to create a new issue, please try the following:

1. Check the Github issues tab if the identified issue has already been reported, if so, please add a +1 to the existing post.
2. Update to the latest version of this code and check if issue has already been fixed
3. Copy and fill in the Bug Report Template we have provided below

### Please use our Bug Report Template

In order to make the process easier, we've included a [sample bug report template](ISSUE_TEMPLATE.md).

<a name="improvements-to-the-codebase"></a>
## Improvements to the Codebase

We welcome direct contributions to the java-http-client code base. Thank you!

### Development Environment ###

#### Install and Run Locally ####

##### Prerequisites #####

- Java version Oracle JDK 8 or OpenJDK 7
- Please see [pom.xml](pom.xml)

##### Initial setup: #####

```bash
git clone https://github.com/sendgrid/java-http-client.git
cd java-http-client
```

##### Execute: #####

See the [examples folder](examples) to get started quickly.

You will need to setup the following environment to use the SendGrid example:

```bash
echo "export SENDGRID_API_KEY='YOUR_API_KEY'" > sendgrid.env
echo "sendgrid.env" >> .gitignore
source ./sendgrid.env
```

```bash
./gradlew build
cd examples
javac -classpath {path_to}/sendgrid-java-http-client-4.2.0-jar.jar:. Example.java && java -classpath {path_to}/sendgrid-java-http-client-4.2.0-jar.jar:. Example
```

<a name="understanding-the-codebase"></a>
## Understanding the Code Base

**/examples**

Working examples that demonstrate usage.

**Client.java**

The main function that does the heavy lifting (and external entry point) is `api`.

**Method.java**

These are the supported Methods.

**Request.java**

Provides a standard interface to an API's HTTP request.

**Response.java**

Provides a standard interface to an API's response.

<a name="testing"></a>
## Testing

All PRs require passing tests before the PR will be reviewed.

All test files are in [`java-http-client/src/test/java/com/sendgrid`](src/test/java/com/sendgrid).

For the purposes of contributing to this repo, please update the [`ClientTest.java`](src/test/java/com/sendgrid/ClientTest.java) file with unit tests as you modify the code.


Run the tests:

```java
./gradlew test -i
```

<a name="style-guidelines-and-naming-conventions"></a>
## Style Guidelines & Naming Conventions

Generally, we follow the style guidelines as suggested by the official language. However, we ask that you conform to the styles that already exist in the library. If you wish to deviate, please explain your reasoning.

Please run your code through:

- [FindBugs](http://findbugs.sourceforge.net/)
- [CheckStyle](http://checkstyle.sourceforge.net/) with [Google's Java Style Guide](http://checkstyle.sourceforge.net/reports/google-java-style.html).

<a name="creating-a-pull-request"></a>
## Creating a Pull Request

1. [Fork](https://help.github.com/fork-a-repo/) the project, clone your fork,
   and configure the remotes:

   ```bash
   # Clone your fork of the repo into the current directory
   git clone https://github.com/sendgrid/java-http-client
   # Navigate to the newly cloned directory
   cd java-http-client
   # Assign the original repo to a remote called "upstream"
   git remote add upstream https://github.com/sendgrid/java-http-client
   ```

2. If you cloned a while ago, get the latest changes from upstream:

   ```bash
   git checkout <dev-branch>
   git pull upstream <dev-branch>
   ```

3. Create a new topic branch (off the main project development branch) to
   contain your feature, change, or fix:

   ```bash
   git checkout -b <topic-branch-name>
   ```

4. Commit your changes in logical chunks. Please adhere to these [git commit
   message guidelines](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)
   or your code is unlikely be merged into the main project. Use Git's
   [interactive rebase](https://help.github.com/articles/interactive-rebase)
   feature to tidy up your commits before making them public.

4a. Create tests.

4b. Create or update the example code that demonstrates the functionality of this change to the code.

5. Locally merge (or rebase) the upstream development branch into your topic branch:

   ```bash
   git pull [--rebase] upstream main
   ```

6. Push your topic branch up to your fork:

   ```bash
   git push origin <topic-branch-name>
   ```

7. [Open a Pull Request](https://help.github.com/articles/using-pull-requests/)
    with a clear title and description against the `main` branch. All tests must be passing before we will review the PR.

## Code Reviews
If you can, please look at open PRs and review them. Give feedback and help us merge these PRs much faster! If you don't know how, GitHub has some great [information on how to review a Pull Request](https://help.github.com/articles/about-pull-request-reviews/).
