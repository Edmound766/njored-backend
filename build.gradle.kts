import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    id("org.flywaydb.flyway") version "12.9.0"
}

tasks.withType<ShadowJar>(){
    // 1. Tell the shadow plugin to merge identical service files instead of overwriting them
    mergeServiceFiles()

    // 2. Resolve conflicting duplicate files across your dependencies (like Flyway)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

group = "com.njored"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
    compilerOptions{
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
        optIn.add("kotlin.time.ExperimentalTime")
    }
}
dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.statusPages)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.datetime)
    implementation(libs.exposed.json)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)
    implementation(libs.database.h2)
    implementation(libs.logback.classic)
    implementation(libs.database.postgresql)
    implementation(libs.database.hikari)
    implementation(ktorLibs.client.contentNegotiation)
    implementation(ktorLibs.client.cio)
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
