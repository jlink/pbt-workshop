# pbt-workshop

Exercises and Solutions for Workshop on Property-based Testing with Java

## Running tests with Gradle

### Unix

```
./gradlew test
```

### Windows

```
gradlew.bat test
```

## Run test always and with reports

```
./gradlew clean test --info
```

## Run specific test container

```
./gradlew clean test --info --tests *.CircularBufferProperties
```

