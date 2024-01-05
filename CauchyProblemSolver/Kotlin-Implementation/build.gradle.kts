plugins {
    kotlin("jvm") version "1.9.21"
}

group = "ru.nsu.fit.sckwo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("com.github.holgerbrandl:kravis:0.8.5")
    implementation("com.github.holgerbrandl:krangl:0.18.4")
    implementation("org.jetbrains.kotlinx:kotlin-jupyter-api:0.12.0-103")
    implementation("com.madgag:animated-gif-lib:1.0")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.5.0")
    implementation("org.jetbrains.lets-plot:lets-plot-batik:4.2.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}