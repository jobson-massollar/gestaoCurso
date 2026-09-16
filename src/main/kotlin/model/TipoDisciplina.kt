package model

enum class TipoDisciplina(val descricao: String) {
    OBRIGATORIA("Obrigatória"),
    OPTATIVA("Optativa"),
    COMPLEMENTAR("Complementar"),
    ELETIVA("Eletiva"),
    ANTIGA("Antiga"),
    EQUIVALENTE("Equivalente"),
    TRANCAMENTO("Trancamento"),
    COMPLEMENTAR_GRD("Disciplina Complementar de Graduação"),
    OUTRA("Outra");

    companion object {
        fun fromDescricao(descricao: String): TipoDisciplina =
            entries.firstOrNull { it.descricao == descricao }
                ?: throw IllegalArgumentException("Descrição de tipo inválida: $descricao")
    }
}