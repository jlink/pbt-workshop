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

## Run all tests always (even when nothing has changed)

```
./gradlew cleanTest test
```

## Run specific test container

```
./gradlew cleanTest test --tests "*.CircularBufferProperties"
```

## Run specific test package

```
./gradlew cleanTest test --tests "pbt.examples.reverse.*"
```


# jqwik Reference Material

- [jqwik cheatsheet](https://johanneslink.net/jqwik-cheatsheet.html)
- [jqwik user guide](https://jqwik.net/docs/current/user-guide.html)