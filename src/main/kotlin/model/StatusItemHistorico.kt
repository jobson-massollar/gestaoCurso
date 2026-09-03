package model

enum class StatusItemHistorico(val codigo: Int) {
    APROVADO(1),
    REPROVADO_POR_NOTA(2),
    REPROVADO_POR_FALTA(3),
    DISPENSA_COM_NOTA(4),
    TRANCAMENTO(5),
    SEM_NOTA(6),
    DISPENSA_SEM_NOTA(7),
    APROVADO_SEM_NOTA(8),
    REPROVADO_SEM_NOTA(9),
    MATRICULADO(10),
    APROVEITAMENTO(11),
    TRANCAMENTO_GERAL(12);

    companion object {
        fun fromCodigo(codigo: Int): StatusItemHistorico =
            entries.firstOrNull { it.codigo == codigo }
                ?: throw IllegalArgumentException("Código de status do item inválido: $codigo")
    }
}