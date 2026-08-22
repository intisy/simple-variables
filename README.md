# Simple Variables

Archives containing JAR files are available as [releases](https://github.com/intisy/simple-variables/releases).

## What is simple-variables?

Simple variable system for java

## Usage in private projects

 * Maven (inside the `pom.xml` file)
```xml
  <repository>
      <id>github</id>
      <url>https://maven.pkg.github.com/intisy/simple-variables</url>
      <snapshots><enabled>true</enabled></snapshots>
  </repository>
  <dependency>
      <groupId>io.github.intisy</groupId>
      <artifactId>simple-variables</artifactId>
      <version>2.0.2.0</version>
  </dependency>
```

 * Maven (inside the `settings.xml` file)
```xml
  <servers>
      <server>
          <id>github</id>
          <username>your-username</username>
          <password>your-access-token</password>
      </server>
  </servers>
```

 * Gradle (inside the `build.gradle.kts` or `build.gradle` file)
```groovy
  repositories {
      maven {
          url "https://maven.pkg.github.com/intisy/simple-variables"
          credentials {
              username = "<your-username>"
              password = "<your-access-token>"
          }
      }
  }
  dependencies {
      implementation 'io.github.intisy:simple-variables:2.0.2.0'
  }
```

## Usage in public projects

 * Gradle (inside the `build.gradle.kts` or `build.gradle` file)
```groovy
  plugins {
      id "io.github.intisy.github-gradle" version "1.3.7"
  }
  dependencies {
      githubImplementation "intisy:simple-variables:2.0.2.0"
  }
```

## License

[![Apache License 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
