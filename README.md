# 

Archives containing JAR files are available as [releases](https://github.com/intisy/simple-variables/releases).

## Usage in private repos (faster)

 * Maven (inside the  file)
```xml
  <repository>
      <id>github</id>
      <url>https://maven.pkg.github.com/intisy/simple-variables</url>
      <snapshots><enabled>true</enabled></snapshots>
  </repository>
  <dependency>
      <groupId>io.github.intisy</groupId>
      <artifactId>simple-variables</artifactId>
      <version>1.8.3</version>
  </dependency>
```

 * Maven (inside the  file)
```xml
  <servers>
      <server>
          <id>github</id>
          <username>your-username</username>
          <password>your-access-token</password>
      </server>
  </servers>
```

 * Gradle (inside the  or  file)
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
      implementation 'io.github.intisy:simple-variables:1.8.3'
  }
```

## Usage in public repos (slower and only works in gradle but safer)

 * Gradle (inside the  or  file)
```groovy
  plugins {
      id "io.github.intisy.github-gradle" version "1.3.7"
  }
  dependencies {
      githubImplementation "intisy:simple-variables:1.8.3"
  }
```

## License

[![Apache License 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
