package model

const val APROVADO = 1
const val DISPENSA = 7
const val APROVADO_SEM_NOTA = 8
const val APROVEITAMENTO = 11

const val OBRIGATORIA = "Obrigatória"
const val OPTATIVA = "Optativa"
const val COMPLEMENTAR = "Complementar"
const val ELETIVA = "Eletiva"
const val ANTIGA = "Antiga"
const val OUTRA = "Outra"

class ItemHistorico(val matricula: String, val ano: Int, val periodo: Int, val descPeriodo: String, val versao: String, val codigo: String, val nome: String, val situacao: Int, val descricao: String, val nota: Float?, val creditos: Int, val horas: Int, val tipo: String): Entity() {

    companion object {
        fun of (matricula: String, ano: Int, periodo: Int, descPeriodo: String, versao: String, codigo: String, nome: String, situacao: Int, descricao: String, nota: Float?, creditos: Int, horas: Int, tipo:String): ItemHistorico =
            ItemHistorico(matricula, ano, periodo, descPeriodo, versao, codigo, nome, situacao, descricao, nota, creditos, horas, tipo)
    }

    override fun toString(): String {
        return "[id=$id matricula=$matricula, ano=$ano, periodo=$periodo, periodo=$descPeriodo, versao=$versao, codigo=$codigo, nome=$nome, situacao=$situacao, descricao=$descricao, nota=$nota, creditos=$creditos, horas=$horas, tipo=$tipo]"
    }
}