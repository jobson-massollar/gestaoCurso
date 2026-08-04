package model

class ItemDiario private constructor(val matricula: String,
                                     val nome: String,
                                     val curso: Int,
                                     val depto: String,
                                     val versao: String,
                                     val codigo: String,
                                     val turma: String): Entity() {

    override fun equals(other: Any?): Boolean =
        if (other is ItemDiario)
            matricula == other.matricula && versao == other.versao && codigo == other.codigo
        else
            false

    override fun hashCode(): Int = (matricula + versao + codigo).hashCode()

    companion object {
        fun of(matricula: String, nome: String, curso: Int, depto: String, versao: String, codigo: String, turma: String) =
            ItemDiario(matricula, nome, curso, depto, versao, codigo, turma)
    }
}