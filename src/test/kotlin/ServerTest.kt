package org.unirio.bsi.coordenacao

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        // loads default configuration
        configure()
        // verify server root returns 200
        val r = client.get("/").status
        assertEquals(HttpStatusCode.OK, r)
    }

    @Test
    fun `versao no application yaml deve bater com a do build gradle`() {
        val buildFile = File("build.gradle.kts").readText()
        val gradleVersion = Regex("""version\s*=\s*"([^"]+)"""")
            .find(buildFile)?.groupValues?.get(1)
            ?: fail("Não encontrei a versão no build.gradle.kts")

        val yamlVersion = ApplicationConfig("application.yaml")
            .property("app.version").getString()

        assertEquals(gradleVersion, yamlVersion,
            "Versão divergente entre build.gradle.kts ($gradleVersion) e application.yaml ($yamlVersion)")
    }
}
