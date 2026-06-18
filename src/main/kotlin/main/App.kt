package main

import adapter.infrastructure.exposed.ExposedDAOFactory
import io.ktor.server.application.*
import services.domain.persistence.DAOFactory
import java.text.Collator
import java.util.*

val appLocale: Locale = Locale.of("pt", "BR")

val collator: Collator = Collator.getInstance(appLocale).apply {
    // PRIMARY ignora acentos e maiúsculas/minúsculas (ex: a = Á = á)
    // SECONDARY ignora maiúsculas, mas diferencia acentos (ex: a = A < á)
    // TERTIARY (Padrão) diferencia maiúsculas e acentos (ex: a < A < á)
    strength = Collator.PRIMARY
}

fun Application.configureApp() {
    DAOFactory.register(ExposedDAOFactory)
}

