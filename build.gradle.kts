plugins {
    id("java")
}

group = "cloud.localstack"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.javassist:javassist:3.29.2-GA")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Premain-Class" to "cloud.localstack.AwsSdkV2DisableCertificateValidation", "Can-Redefine-Classes" to true, "Can-Retransform-Classes" to true))
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
