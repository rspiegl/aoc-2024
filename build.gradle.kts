plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

dependencies {
    implementation("io.ktor:ktor-client-core:3.0.2")
    implementation("org.hipparchus:hipparchus-core:3.1")
    implementation("io.ktor:ktor-client-cio:3.0.2")
    implementation("org.slf4j:slf4j-log4j12:2.0.16")
}
