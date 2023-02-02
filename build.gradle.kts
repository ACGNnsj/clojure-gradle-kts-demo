import dev.clojurephant.plugin.clojure.tasks.ClojureCompile
import groovy.xml.dom.DOMCategory.attributes

plugins {
    id("dev.clojurephant.clojure") version "0.7.0"
    id("application")
    id("maven-publish")
    id("signing")
}

val systemProperties = System.getProperties()
println("systemProperties: $systemProperties")
//ext["signing.keyId"] = systemProperties["signing.keyId"]
//ext["signing.password"] = systemProperties["signing.password"]
//ext["signing.secretKeyRingFile"] = systemProperties["signing.secretKeyRingFile"]
var ossrhUsername = ""
var ossrhPassword = ""
var signingKey = ""
var signingPassword = ""
println("isWindows: ${org.gradle.internal.os.OperatingSystem.current().isWindows}")
if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
    ossrhUsername = systemProperties["ossrhUsername"] as String
    ossrhPassword = systemProperties["ossrhPassword"] as String
    signingKey = systemProperties["signingKey"] as String
    signingPassword = systemProperties["signingPassword"] as String
} else {
    ossrhUsername = System.getenv("ossrhUsername")
    ossrhPassword = System.getenv("ossrhPassword")
    signingKey = System.getenv("signingKey")
    signingPassword = System.getenv("signingPassword")
}
println(signingKey.contains("lIYEY9oMWxYJKwYBBAHaRw8BAQdAZdyZAKkdZM"))
println(signingKey.contains("\n"))
println(signingKey.contains("\r"))
println(signingKey.contains("\\n"))
println(
    ("lIYEY9oMWxYJKwYBBAHaRw8BAQdAZdyZAKkdZMU98tAGq+iLAYbzRTK0JfMK6hvU\n" +
            "4RTpoSn+BwMCxM+FZQAPqS/OsdBZ5Ejx65c2VLiiF0hqsLQ+OofsMgg3cQlLHjZ8\n" +
            "h8JuXTV4u+IXY2S8ntSk+tRT2pRaebn/Hb9g35/DC92Y5plyU/Py97QaQUNHTU4g\n" +
            "PG9vdG9wb29AdmlwLnFxLmNvbT6ImQQTFgoAQRYhBG5uERqOHBbLegVVO/uIXpIt\n" +
            "QioWBQJj2gxbAhsDBQkDw4/lBQsJCAcCAiICBhUKCQgLAgQWAgMBAh4HAheAAAoJ\n" +
            "EPuIXpItQioWF2sA/3AHqhXkM62Hpp//nnKHcqQYXO+d81jZgNWkO5H9eVYFAQCs\n" +
            "533eK3zwvmRHhaKhWw05Dyk20F1VWX/i9zhKKftTApyLBGPaDFsSCisGAQQBl1UB\n" +
            "BQEBB0DotNIkgTjvXtw68Fs02fdXQrL7f8fRqZayCFDM0ABuBQMBCAf+BwMCU1DC\n" +
            "I/soDOTOtAU9wb/CyOM3CG3My9n/609F+75t/sVR6kYPh6+ztPtqSrIZC/63Dh7T\n" +
            "f83upMsIOPeUOKA2eP6t7xNNe6ep4kfXXsXjL4h+BBgWCgAmFiEEbm4RGo4cFst6\n" +
            "BVU7+4heki1CKhYFAmPaDFsCGwwFCQPDj+UACgkQ+4heki1CKhZU9AD/axprT9zb\n" +
            "icv91IGUJiwo5ZSLsvDjIr2xvImrlm0LBkIBAMiVGc1ZwQ7lxHTXx4F/eCnukTD7\n" +
            "9ZBWinrS7vLAuREN\n" +
            "=so4m") == signingKey
)

group = "io.github.acgnnsj"
version = "1.0.0"
val artifactIdentifier = "clojure-java"
val clojureMainClassName = "main.hello"
val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"

repositories {
    mavenCentral()
    maven { url = uri("https://clojars.org/repo") }
}
dependencies {
    implementation("org.clojure:clojure:1.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}
clojure {
    builds.maybeCreate("main").aotAll()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
/*var count: Int = 0
tasks.withType<ClojureCompile> {
    println("${count++}: compileClojure")
    println("${System.getenv()["HOMEDRIVE"]}")
}*/


//sourceSets {
//    main {
//        withConvention(ClojureSourceSet::class) {
//            clojure.srcDir("src/main/clojure")
//        }
//    }
//}
application {
    mainClassName = clojureMainClassName
}
tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to clojureMainClassName,
                "Implementation-Title" to artifactIdentifier,
                "Implementation-Version" to archiveVersion
            )
        )
    }
}
val sourceJar by tasks.registering(Jar::class) {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}
val javadocJar by tasks.registering(Jar::class) {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = group.toString()
                artifactId = artifactIdentifier
//                version = "1.0.0"
                artifact(sourceJar)
                artifact(javadocJar)
//                afterEvaluate { artifact(tasks.getByName("jar")) }
                from(components["java"])
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
                pom {
                    name.set("Clojure-Java Lib")
                    description.set("A Clojure-Java library built by Gradle Kotlin DSL")
                    url.set("https://github.com/ACGNnsj/clojure-gradle-kts-demo")
                    /*properties.set(
                        mapOf(
                            "myProp" to "value",
                            "prop.with.dots" to "anotherValue"
                        )
                    )*/
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("ACGMN")
                            name.set("Shijie Ni")
                            email.set("ootopoo@vip.qq.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/ACGNnsj/clojure-gradle-kts-demo.git")
                        developerConnection.set("scm:git:https://github.com/ACGNnsj/clojure-gradle-kts-demo.git")
                        url.set("https://github.com/ACGNnsj/clojure-gradle-kts-demo")
                    }
                }
            }
        }
        repositories {
            maven {
                isAllowInsecureProtocol = false
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }
    signing {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["mavenJava"])
    }
}


