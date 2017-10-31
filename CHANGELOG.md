# Change Log
All notable changes to this project will be documented in this file.

This project adheres to [Semantic Versioning](http://semver.org/).

## [4.2.0] - 2017-10-30
### Added
- [Pull #22](https://github.com/sendgrid/java-http-client/pull/22): Allow setting both `apache http client` and `test` parameters
- BIG thanks to [Maxim Novak](https://github.com/maximn) for the pull request!

## [4.1.1] - 2016-10-11
### Added
- [Pull #23](https://github.com/sendgrid/java-http-client/pull/23): Moved Mockito to test dependency.
- BIG thanks to [Joseph Lust](https://github.com/twistedpair) for the pull request!

## [4.1.0] - 2016-10-11
### Added
- [Pull #17](https://github.com/sendgrid/java-http-client/pull/17): Assign server response outside try block
- BIG thanks to [Michael Dempsey](https://github.com/bluestealth) for the pull request!

## [4.0.0] - 2016-10-11
### BREAKING Change
- [Pull #14](https://github.com/sendgrid/java-http-client/pull/14): Make response have private variables
- Fixed [Issue #12](https://github.com/sendgrid/java-http-client/issues/12): The public Response variables should be private
- The breaking change is that variables that were public are now private and accessable only via getters and setters
- BIG thanks to [Diego Camargo](https://github.com/belfazt) for the pull request!

## [3.0.0] - 2016-10-06
### BREAKING Change
- [Pull #15](https://github.com/sendgrid/java-http-client/pull/15): Update the request object with sensible defaults and access methods
- Fixes [Issue #13](https://github.com/sendgrid/java-http-client/issues/13): Update the Request object with sensible defaults and access methods
- The breaking change is that variables that were public are now private and accessable only via getters and setters
- BIG thanks to [Diego Camargo](https://github.com/belfazt) for the pull request!

## [2.3.4] - 2016-08-09
### Fix
- [Pull #7](https://github.com/sendgrid/java-http-client/pull/7): Fix Response Charset to UTF-8
- Fixes [issue #6](https://github.com/sendgrid/java-http-client/issues/6): Multi-byte character got garbled on received mail
- BIG thanks to [Yoichi Kikuta](https://github.com/kikutaro) for the pull request!

## [2.3.3] - 2016-08-08
### Added
- Pull request [#11](https://github.com/sendgrid/java-http-client/pull/11)
- Solves [issue #10](https://github.com/sendgrid/java-http-client/issues/10): Improve Error Handling
- Now error messages are passed through from the server
- BIG thanks to [shuron](https://github.com/shuron) / [Alexander Holbreich](https://github.com/aholbreich) for the pull request!

## [2.3.2] - 2016-07-18
### Fixed
- 2.3.1 did not upload correctly

## [2.3.1] - 2016-07-08
### Fixed
- [Fix charset: Use "UTF-8" charset instead of default "ISO-8859-1"](https://github.com/sendgrid/java-http-client/pull/5)
- Thanks [DanailMinchev](https://github.com/DanailMinchev)!

## [2.3.0] - 2016-06-10
### Added
- Automatically add Content-Type: application/json when there is a request body

## [2.2.1] - 2016-06-08
### Fixed
- Set client properly when testing

## [2.2.0] - 2016-06-08
### Added
- Can pass test flag to allow for http calls

## [2.1.0] - 2016-06-08
### Added
- DELETE can now have a request body

## [2.0.0] - 2016-06-03
### Changed
- Made the Request and Response variables non-redundant. e.g. request.requestBody becomes request.body

## [1.0.0] - 2016-04-15
### Added
- We are live!
