/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.8.2/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.4.31"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
    jacoco
    java
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
    flatDir{ dirs("../../unibolibs")   }   //Our libraries
}

version="1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    //COROUTINE
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")

    //OkHttp library for websockets with Kotlin
    implementation( "com.squareup.okhttp3:okhttp:4.9.0" )
    //Ktor is a framework for quickly creating web applications in Kotlin with minimal effort.
    implementation("io.ktor:ktor:1.5.1")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    //HTTP
    // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    //JSON
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20201115" )
    // https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple
    //implementation("com.googlecode.json-simple:json-simple:1.1.1")
    //SOCKET.IO
    //implementation("com.github.nkzawa:socket.io-client:0.6.0")
    // https://mvnrepository.com/artifact/javax.websocket/javax.websocket-api
    implementation("javax.websocket:javax.websocket-api:1.1")   //javax.websocket api is only the specification
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:1.9")
    //UNIBO
    implementation( "uniboIssSupport:IssWsHttpJavaSupport" )
}

application {
    // Define the main class for the application.
    mainClass.set("it.unibo.cautiousExplorerActors.App")
}
