/*
 * Copyright (c) 2024, Francesco Magnani
 * and all authors listed in the `build.gradle.kts` and the generated `pom.xml` file.
 *
 *  This file is part of Subjekt, and is distributed under the terms of the Apache License 2.0, as described in the
 *  LICENSE file in this project's repository's top directory.
 *
 */
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.antlr.kotlin)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.npm.publish)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.taskTree)
    alias(libs.plugins.mavenPublish)
}

group = "io.github.freshmag"

repositories {
    google()
    mavenCentral()
}

multiJvm {
    jvmVersionForCompilation.set(21)
}

kotlin {
    jvmToolchain(21)

    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_1_8
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {}
        }

        commonTest.dependencies {
            implementation(libs.bundles.kotlin.testing.common)
            implementation(libs.bundles.kotest.common)
        }

        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
    }

    js(IR) {
        moduleName = "moirakt"
        browser()
        nodejs()
        binaries.library()
    }

    applyDefaultHierarchyTemplate()

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    allWarningsAsErrors = true
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }
}

ktlint {
    filter {
        exclude("**/generated/**")
    }
}

publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/FreshMag/moirakt")
            credentials(PasswordCredentials::class)
        }
    }
}

mavenPublishing {
    pom {
        name.set("moirakt")
        description.set("A Kotlin Multiplatform library for calculating probability distributions.")
        inceptionYear.set("2025")
        url.set("https://github.com/FreshMag/moirakt")

        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        // Specify developer information
        developers {
            developer {
                id.set("FreshMag")
                name.set("Francesco Magnani")
                email.set("pescomagnani@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/FreshMag/moirakt")
        }
    }
    // Enable GPG signing for all publications
    signAllPublications()

    if (System.getenv("CI") == "true") {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = false)
    }
}

detekt {
    config.from(".detekt.yml")
    buildUponDefaultConfig = true
    parallel = true
}

npmPublish {
    packages {
        named("js") {
            packageName = "moirakt"
        }
    }

    registries {
        register("npmjs") {
            uri.set("https://registry.npmjs.org")
            if (System.getenv("CI") == "true") {
                authToken.set(System.getenv("NPM_TOKEN"))
            } else {
                val npmToken: String? by project
                authToken.set(npmToken)
                dry.set(npmToken.isNullOrBlank())
            }
        }
    }
}
