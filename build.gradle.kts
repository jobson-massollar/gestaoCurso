import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
}

group = "org.unirio.bsi.coordenacao"
version = "1.0.0"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(23)
}

// Define a JRE, o nome e a tag na imagem do docker
ktor {
    docker {
        jreVersion = JavaVersion.VERSION_23
        localImageName.set("gestao-bsi-ktor-docker-image")
        imageTag.set(project.version.toString())
    }
}

dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.htmlBuilder)
    implementation(ktorLibs.server.htmx)
    implementation(ktorLibs.htmx)
    implementation(ktorLibs.htmx.html)
    implementation(ktorLibs.server.netty)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlinDatetime)
    implementation(libs.logback.classic)
    implementation(libs.postgresql)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}

// Define a visibilidade das propriedades que serão documentadas
dokka {
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(
            setOf(
                VisibilityModifier.Public,
                VisibilityModifier.Protected,
                VisibilityModifier.Internal,
                VisibilityModifier.Private
            )
        )
    }
}
