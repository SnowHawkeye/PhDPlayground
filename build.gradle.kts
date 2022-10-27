import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "org.icyjam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.jena:apache-jena-libs:4.6.1")
    implementation("edu.stanford.protege:protege-editor-owl:5.5.0")
    implementation("edu.stanford.protege:protege-editor-core:5.5.0")
    implementation("edu.stanford.protege:protege-common:5.5.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}