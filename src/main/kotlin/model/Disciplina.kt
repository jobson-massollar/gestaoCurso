package model

class Disciplina private constructor(val versao: String, val codigo: String, val nome: String, val periodo: Int, val creditos: Int, val horas: Int, val tipo: String): Entity() {

    val preRequisitos by lazy {
        RepositoryFactory.get(DisciplinaRepository::class).findPreRequisitos(this)
    }

    companion object {
        fun of(versao: String, codigo: String, nome: String, periodo: Int, creditos: Int, horas: Int, tipo: String) =
            Disciplina(versao, codigo, nome, periodo, creditos, horas, tipo)
    }

    override fun equals(other: Any?): Boolean =
        if (other is Disciplina)
            versao == other.versao && codigo == other.codigo
        else
            false

    override fun hashCode() = (versao + codigo).hashCode()

    override fun toString(): String = "[id=$id versao=$versao codigo=$codigo nome=$nome periodo=$periodo creditos=$creditos horas=$horas tipo=$tipo"
}