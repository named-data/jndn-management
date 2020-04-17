# Developer Notes

## Compile

To compile:

    ./gradlew assemble

Note: when compiling on Windows, replace `./gradlew` with `gradlew` or `gradlew.bat`.

To build documentation:

    ./gradlew javadoc

To build all artifacts and publish to a local maven repository:

    ./gradlew install

To publish to maven repository `signing.keyId`, `signing.password`, `signing.secretKeyRingFile`,
`ossrhUsername`, and `ossrhPassword` variables need to be defined in project-specific or
user-specific `gradle.properties` file.  After the variables defined, run the following command
to build, sign, and upload archives to maven:

    ./gradlew uploadArchives

To get list of other targets, use `./gradlew tasks`.

## Tests

The package contains two types of tests: unit and integration.  The integration tests require
NFD instance to be running locally.

### Unit Tests

To run unit tests:

    ./gradlew test

To run a specific test or test case, use `--tests=<test-name>[.<test-case>]` command-line option. For example,

    ./gradlew test --tests *FaceStatusTest
    ./gradlew test --tests *FaceStatusTest.testEncode

### Integration Tests

To run integration tests

    ./gradlew integrationTest
