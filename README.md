Downloads
---------
Archives containing JAR files are available as [releases](https://github.com/Blizzity/Blizzity/SimpleVariables/releases).

## Usage in private repos (faster)

 * Maven (inside the  file)
```xml
  <repository>
      <id>github</id>
      <url>https://maven.pkg.github.com/Blizzity/Blizzity/SimpleVariables</url>
      <snapshots><enabled>true</enabled></snapshots>
  </repository>
  <dependency>
      <groupId>io.github.intisy</groupId>
      <artifactId>Blizzity/SimpleVariables</artifactId>
      <version>1.7.1</version>
  </dependency>
```

 * Maven (inside the  file)
```xml
  <servers>
      <server>
          <id>github</id>
          <username><your-username></username>
          <password><your-access-token></password>
      </server>
  </servers>
```

 * Gradle (inside the  or  file)
```groovy
  repositories {
      maven {
          url "https://maven.pkg.github.com/Blizzity/Blizzity/SimpleVariables"
          credentials {
              username = "<your-username>"
              password = "<your-access-token>"
          }
      }
  }
  dependencies {
      implementation 'io.github.intisy:Blizzity/SimpleVariables:1.7.1'
  }
```

## Usage in public repos (slower and only works in gradle but safer)

 * Gradle (inside the  or  file)
```groovy
  plugins {
      id "io.github.intisy.github-gradle" version "1.1"
  }
  dependencies {
      githubImplementation "intisy:Blizzity/SimpleVariables:1.7.1"
  }
```
