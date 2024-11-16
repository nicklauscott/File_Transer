plugins {
    kotlin("jvm") version "2.0.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.apache.ftpserver:ftpserver-core:1.2.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    // mainClass.set("MainKt") // this didn't work
    mainClass.set("org.example.MainKt") //
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17" // Or any other target JVM version
    }

    jar {

        manifest {
            // attributes["Main-Class"] = "MainKt" // replace MainKt to org.example.MainKt
            attributes["Main-Class"] = "org.example.MainKt" // Specify the main class here
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) } })
    }
}






