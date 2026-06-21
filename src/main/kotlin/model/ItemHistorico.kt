package model

const val APROVADO = 1
const val REPROVADO_POR_NOTA = 2
const val REPROVADO_POR_FALTA = 3
const val DISPENSA_COM_NOTA = 4
const val TRANCAMENTO = 5
const val SEM_NOTA = 6
const val DISPENSA_SEM_NOTA = 7
const val APROVADO_SEM_NOTA = 8
const val REPROVADO_SEM_NOTA = 9
const val MATRICULADO = 10
const val APROVEITAMENTO = 11
const val TRANCAMENTO_GERAL = 12

const val OBRIGATORIA = "Obrigatória"
const val OPTATIVA = "Optativa"
const val COMPLEMENTAR = "Complementar"
const val ELETIVA = "Eletiva"
const val ANTIGA = "Antiga"
const val OUTRA = "Outra"

class ItemHistorico(val matricula: String, val ano: Int, val periodo: Int, val descPeriodo: String, val versao: String, val codigo: String, val nome: String, val situacao: Int, val descricao: String, val nota: Float?, val creditos: Int, val horas: Int, val tipo: String): Entity() {

    val isAprovado = situacao == APROVADO || situacao == DISPENSA_SEM_NOTA || situacao == DISPENSA_COM_NOTA || situacao == APROVADO_SEM_NOTA || situacao == APROVEITAMENTO
    val isReprovado = situacao == REPROVADO_POR_NOTA || situacao == REPROVADO_POR_FALTA || situacao == REPROVADO_SEM_NOTA
    val isTrancamento = situacao == TRANCAMENTO_GERAL
    val isMatriculado = situacao == MATRICULADO

    companion object {
        fun of (matricula: String, ano: Int, periodo: Int, descPeriodo: String, versao: String, codigo: String, nome: String, situacao: Int, descricao: String, nota: Float?, creditos: Int, horas: Int, tipo:String): ItemHistorico =
            ItemHistorico(matricula, ano, periodo, descPeriodo, versao, codigo, nome, situacao, descricao, nota, creditos, horas, tipo)
    }

    override fun toString(): String {
        return "[id=$id matricula=$matricula, ano=$ano, periodo=$periodo, periodo=$descPeriodo, versao=$versao, codigo=$codigo, nome=$nome, situacao=$situacao, descricao=$descricao, nota=$nota, creditos=$creditos, horas=$horas, tipo=$tipo]"
    }
}

val List<ItemHistorico>.obrigatorias: List<ItemHistorico>
    get() = this.filter { it.tipo == OBRIGATORIA }

val List<ItemHistorico>.optativas: List<ItemHistorico>
    get() = this.filter { it.tipo == OPTATIVA }

val List<ItemHistorico>.complementares: List<ItemHistorico>
    get() = this.filter { it.tipo == COMPLEMENTAR }

val List<ItemHistorico>.eletivas: List<ItemHistorico>
    get() = this.filter { it.tipo == ELETIVA }

val List<ItemHistorico>.aprovadas: List<ItemHistorico>
    get() = this.filter { it.isAprovado }

val List<ItemHistorico>.reprovadas: List<ItemHistorico>
    get() = this.filter { it.isReprovado }

val List<ItemHistorico>.matriculadas: List<ItemHistorico>
    get() = this.filter { it.isMatriculado }

fun List<ItemHistorico>.cursadas(periodo: Periodo): List<ItemHistorico> =
    this.filter { it.ano == periodo.ano && it.periodo == periodo.semestre }