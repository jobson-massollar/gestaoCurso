package main

import adapter.infrastructure.exposed.ExposedDAOFactory
import io.ktor.server.application.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import services.domain.persistence.DAOFactory
import java.text.Collator
import java.util.*
import kotlin.time.Clock

val appLocale: Locale = Locale.of("pt", "BR")

val collator: Collator = Collator.getInstance(appLocale).apply {
    // PRIMARY ignora acentos e maiúsculas/minúsculas (ex: a = Á = á)
    // SECONDARY ignora maiúsculas, mas diferencia acentos (ex: a = A < á)
    // TERTIARY (Padrão) diferencia maiúsculas e acentos (ex: a < A < á)
    strength = Collator.PRIMARY
}

val fileTimestampFormat: DateTimeFormat<LocalDateTime>
    get() = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()
        char(' ')
        hour()
        minute()
        second()
    }

val UITimestampFormat: DateTimeFormat<LocalDateTime>
    get() = LocalDateTime.Format {
        day()
        char('/')
        monthNumber()
        char('/')
        year()
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
    }

val dateFormat = LocalDate.Format {
    day()
    char('/')
    monthNumber()
    char('/')
    year()
}
val timeFormat = LocalTime.Format {
    hour()
    char(':')
    minute()
    char(':')
    second()
}

fun currentDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun Application.configureApp() {
    DAOFactory.register(ExposedDAOFactory)
}

