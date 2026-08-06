package model

class TotalizacaoInscricao(val codigo: String,
                           val nome: String,
                           val turma: String,
                           val solicitados: Int,
                           val aceitos: Int,
                           val faltaPreRequisito: Int,
                           val faltaVagas: Int,
                           val cancelados: Int): Entity() {

    override fun equals(other: Any?): Boolean =
        if (other is Inscricao)
            codigo == other.codigo
        else
            false

    override fun hashCode() = codigo.hashCode()

    companion object {
        fun of(codigo: String, nome: String, turma: String, solicitados: Int, aceitos: Int, faltaPreRequisito: Int, faltaVagas: Int, cancelados: Int) =
            TotalizacaoInscricao(codigo, nome, turma, solicitados, aceitos, faltaPreRequisito, faltaVagas, cancelados)
    }
}