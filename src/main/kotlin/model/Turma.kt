package model

class Turma private constructor(val codigo: String, val inscritos: Int): Entity() {

    val disciplinas: MutableList<Disciplina> = mutableListOf()

//    val disciplinas: List<Disciplina>
//        get() = _disciplinas

    companion object {
        fun of(codigo: String, inscritos: Int) = Turma(codigo, inscritos)
    }

    fun addDisciplina(disciplina: Disciplina) = disciplinas.add(disciplina)

    override fun equals(other: Any?): Boolean =
        if (other is Turma)
            return this.codigo == other.codigo
        else
            false

    override fun hashCode(): Int = codigo.hashCode()
}