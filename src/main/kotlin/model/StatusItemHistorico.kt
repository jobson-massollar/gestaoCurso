package model

enum class StatusItemHistorico(val codigo: Int, val descricao: String) {
    APROVADO(1, "Aprovado"),
    REPROVADO_POR_NOTA(2, "Repr. Nota"),
    REPROVADO_POR_FALTA(3, "Repr. Falta"),
    DISPENSA_COM_NOTA(4, "Dispensa"),
    TRANCAMENTO(5, "Trancamento"),
    SEM_NOTA(6, "Sem nota"),
    DISPENSA_SEM_NOTA(7, "Dispensa s/ Nota"),
    APROVADO_SEM_NOTA(8, "Aprov. s/ Nota"),
    REPROVADO_SEM_NOTA(9, "Repr. s/ Nota"),
    MATRICULADO(10, "Matriculado"),
    APROVEITAMENTO(11, "Aproveitamento"),
    TRANCAMENTO_GERAL(12, "Trancamento Geral");

    companion object {
        fun fromCodigo(codigo: Int): StatusItemHistorico =
            entries.firstOrNull { it.codigo == codigo }
                ?: throw IllegalArgumentException("Código de status do item inválido: $codigo")
    }
}