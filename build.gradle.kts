plugins {
    id("java")
}

group = "wildepizza.com.github.simplevariables"

repositories {
    mavenCentral()
}

tasks.jar {
    archiveFileName.set("SimpleVariables.jar") // Set the name of the JAR file here
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}