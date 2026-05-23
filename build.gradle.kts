import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    base
    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "com.clinicflow"
version = "0.1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    group = rootProject.group
    version = rootProject.version

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    the<DependencyManagementExtension>().imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

project(":app") {
    apply(plugin = "org.springframework.boot")
}
