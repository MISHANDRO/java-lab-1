plugins {
    id("java")
}

group = "ru.mishandro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.jetbrains:annotations:16.0.1")
}

tasks.test {
    useJUnitPlatform()
}