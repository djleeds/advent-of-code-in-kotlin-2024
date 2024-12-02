plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    implementation("com.willowtreeapps.assertk:assertk:0.28.1")

}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}
