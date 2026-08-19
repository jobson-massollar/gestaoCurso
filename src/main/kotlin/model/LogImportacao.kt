package model

import kotlinx.datetime.LocalDateTime

class LogImportacao(val dtProcessamento: LocalDateTime): Entity() {

    override fun equals(other: Any?): Boolean =
        if (other is LogImportacao)
            dtProcessamento == other.dtProcessamento
        else
            false

    override fun hashCode() = dtProcessamento.hashCode()

    companion object {
        fun of(dtProcessamento: LocalDateTime) = LogImportacao(dtProcessamento)
    }
}