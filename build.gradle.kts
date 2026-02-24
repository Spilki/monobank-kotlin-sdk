import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.nexus.publish)
    `java-library`
    `maven-publish`
    signing
}

group = property("GROUP") as String
version = property("VERSION_NAME") as String

repositories {
    mavenCentral()
}

dependencies {
    api(libs.bundles.jackson)
    implementation(libs.slf4j.api)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.bundles.test)
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-opt-in=kotlin.RequiresOptIn",
        )
    }
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("detekt.yml"))
}

ktlint {
    version.set("1.5.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = property("GROUP") as String
            artifactId = property("ARTIFACT_ID") as String
            version = property("VERSION_NAME") as String

            pom {
                name.set("Monobank Kotlin SDK")
                description.set(
                    "Kotlin SDK for Monobank Acquiring API — invoice management, " +
                        "subscriptions, wallet tokenization, QR payments, webhooks, and more.",
                )
                url.set("https://github.com/Spilki/monobank-kotlin-sdk")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("damienmiheev")
                        name.set("Dmytro Mikhieiev")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Spilki/monobank-kotlin-sdk.git")
                    developerConnection.set("scm:git:ssh://github.com/Spilki/monobank-kotlin-sdk.git")
                    url.set("https://github.com/Spilki/monobank-kotlin-sdk")
                }
            }
        }
    }
}

signing {
    val signingKey = findProperty("signingKey") as String? ?: System.getenv("GPG_SIGNING_KEY")
    val signingPassword = findProperty("signingPassword") as String? ?: System.getenv("GPG_SIGNING_PASSWORD")
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["maven"])
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME"))
            password.set(findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD"))
        }
    }
}
