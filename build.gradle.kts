import dev.clojurephant.plugin.clojure.tasks.ClojureCompile
import groovy.xml.dom.DOMCategory.attributes

//import clojuresque.tasks.ClojureSourceSet

/*buildscript {
    repositories {
        maven { url = uri("https://clojars.org/repo") }
        mavenCentral()
    }
    dependencies {
        classpath("clojuresque:clojuresque:1.7.0")
    }
}*/
//apply(plugin = "clojure")
plugins {
    id("dev.clojurephant.clojure") version "0.7.0"
//    id("java")
//    id("clojure")
//    clojure("1.7.0")
    id("application")
    id("maven-publish")
}

group = "io.github.acgnnsj"
version = "1.0-SNAPSHOT"
val artifactIdentifier = "clojure-java"
val clojureMainClassName = "main.hello"

repositories {
    mavenCentral()
    maven { url = uri("https://clojars.org/repo") }
//    dependencies {
//        classpath("com.android.tools.build:gradle:4.1.2")
//        classpath("clojuresque:clojuresque:1.7.0")
//        classpath("io.github.gradle-clojure:gradle-clojure-plugin:0.5.0-alpha.3")
//    }
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
var count: Int = 0
tasks.withType<ClojureCompile> {
    println("${count++}: compileClojure")
    println("${System.getenv()["HOMEDRIVE"]}")
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

val sourceJar by tasks.registering(Jar::class) {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(sourceJar)
            afterEvaluate { artifact(tasks.getByName("jar")) }
            groupId = group.toString()
            artifactId = artifactIdentifier
//            println(this)
//            println(this.version)
//            println(it)
//            version = "1.0.0"
        }
    }
    repositories {}
}

