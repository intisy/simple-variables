Downloads
---------
Archives containing JAR files are available as [releases](https://github.com/Blizzity/Blizzity/SimpleVariables/releases).

 * Maven (inside the  file)
```xml
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/Blizzity/Blizzity/SimpleVariables</url>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
  <dependency>
    <groupId>com.github.WildePizza</groupId>
    <artifactId>simple-variables</artifactId>
    <version>1.5.7</version>
  </dependency>
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
      implementation 'com.github.WildePizza:simple-variables:1.5.7'
  }
```
