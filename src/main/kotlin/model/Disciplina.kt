package model


class Disciplina private constructor(val versao: String,
                                     val codigo: String,
                                     val nome: String,
                                     val periodo: Int,
                                     val creditos: Int,
                                     val horas: Int,
                                     val tipo: String,
                                     val inscritos: Int): Entity() {

    // private val cacheItensDiario = mutableMapOf<String, List<ItemDiario>>()

    val isObrigatoria: Boolean = tipo == "Obrigatória";

    val preRequisitos by lazy {
        RepositoryFactory.get(DisciplinaRepository::class).findPreRequisitos(this)
    }

    val matriculadosCurso by lazy {
        RepositoryFactory.get(AlunoRepository::class).findByDisciplina(this)
    }

    val podemCursar by lazy {
        RepositoryFactory.get(AlunoRepository::class).findPodemCursar(this)
    }

    val recusadosFaltaVaga by lazy {
        RepositoryFactory.get(AlunoRepository::class).findBySituacaoInscricao(this, FALTA_VAGA)
    }

//    fun itensDiario(turma: String): List<ItemDiario> =
//        cacheItensDiario.getOrPut(turma) {
//            RepositoryFactory.get(ItemDiarioRepository::class).findByTurmaDisciplina(turma, this)
//        }

    companion object {
        fun of(versao: String, codigo: String, nome: String, periodo: Int, creditos: Int, horas: Int, tipo: String, inscritos: Int) =
            Disciplina(versao, codigo, nome, periodo, creditos, horas, tipo, inscritos)
    }

    override fun equals(other: Any?): Boolean =
        if (other is Disciplina)
            versao == other.versao && codigo == other.codigo
        else
            false

    override fun hashCode() = (versao + codigo).hashCode()

}