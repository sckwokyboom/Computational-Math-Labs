import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "ru.nsu.fit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks {
    named<ShadowJar>("shadowJar") {
        from(project.sourceSets.main.get().output)
        configurations = project.configurations.filter { it.name == "runtimeClasspath" }
        archiveFileName.set("IntegrationCalculation.jar")
        destinationDirectory.set(file("./out/artifacts"))
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}