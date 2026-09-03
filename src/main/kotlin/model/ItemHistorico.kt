package model

import model.Grade.Grade2023

class ItemHistorico private constructor(val matricula: String,
                                        val ano: Int,
                                        val periodo: Int,
                                        val descPeriodo: String,
                                        val versao: String,
                                        val codigo: String,
                                        val nome: String,
                                        val situacao: StatusItemHistorico,
                                        val descricao: String,
                                        val nota: Float?,
                                        val creditos: Int,
                                        val horas: Int,
                                        val tipo: TipoDisciplina): Entity() {

    val isAprovado = situacao == StatusItemHistorico.APROVADO ||
                     situacao == StatusItemHistorico.DISPENSA_SEM_NOTA ||
                     situacao == StatusItemHistorico.DISPENSA_COM_NOTA ||
                     situacao == StatusItemHistorico.APROVADO_SEM_NOTA ||
                     situacao == StatusItemHistorico.APROVEITAMENTO
    val isReprovado = situacao ==  StatusItemHistorico.REPROVADO_POR_NOTA ||
                      situacao ==  StatusItemHistorico.REPROVADO_POR_FALTA ||
                      situacao ==  StatusItemHistorico.REPROVADO_SEM_NOTA
    val isTrancamento = situacao ==  StatusItemHistorico.TRANCAMENTO_GERAL
    val isMatriculado = situacao ==  StatusItemHistorico.MATRICULADO

    val disciplina by lazy {
        RepositoryFactory.get(DisciplinaRepository::class).findByItemHistorico(this);
    }

    companion object {
        fun of (matricula: String, ano: Int, periodo: Int, descPeriodo: String, versao: String, codigo: String, nome: String, situacao:  StatusItemHistorico, descricao: String, nota: Float?, creditos: Int, horas: Int, tipo:TipoDisciplina): ItemHistorico =
            ItemHistorico(matricula, ano, periodo, descPeriodo, versao, codigo, nome, situacao, descricao, nota, creditos, horas, tipo)
    }

    override fun equals(other: Any?): Boolean =
        if (other is ItemHistorico)
            matricula == other.matricula &&
            ano == other.ano &&
            periodo == other.periodo &&
            codigo == other.codigo
        else
            false

    override fun hashCode(): Int = (matricula + ano.toString() + periodo.toString() + codigo).hashCode()
}

val List<ItemHistorico>.obrigatorias: List<ItemHistorico>
    get() = this.filter { it.tipo == TipoDisciplina.OBRIGATORIA }

val List<ItemHistorico>.optativas: List<ItemHistorico>
    get() = this.filter { it.tipo == TipoDisciplina.OPTATIVA }

val List<ItemHistorico>.complementares: List<ItemHistorico>
    get() = this.filter { it.tipo == TipoDisciplina.COMPLEMENTAR }

val List<ItemHistorico>.eletivas: List<ItemHistorico>
    get() = this.filter { it.tipo == TipoDisciplina.ELETIVA }

val List<ItemHistorico>.eletivasAproveitadas: List<ItemHistorico>
    get() = this.filter { it.tipo == TipoDisciplina.ELETIVA }.sortedByDescending { it.horas }.take(2)

val List<ItemHistorico>.horasEletivasAproveitadas: Int
    get() = minOf(this.sumOf { it.horas }, Grade2023.horasEletivas)

fun List<ItemHistorico>.cursadas(periodo: Periodo): List<ItemHistorico> =
    this.filter { it.ano == periodo.ano && it.periodo == periodo.semestre }

val List<ItemHistorico>.aprovadas: List<ItemHistorico>
    get() = this.filter { it.isAprovado }

val List<ItemHistorico>.reprovadas: List<ItemHistorico>
    get() = this.filter { it.isReprovado }

val List<ItemHistorico>.matriculadas: List<ItemHistorico>
    get() = this.filter { it.isMatriculado }

val List<ItemHistorico>.trancados: List<Periodo>
    get() = this.filter { it.isTrancamento }.map {
        Periodo(it.ano, it.periodo)
    }.filter { it !in INICIO_PANDEMIA..FIM_PANDEMIA }