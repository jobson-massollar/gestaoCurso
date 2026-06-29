package model

sealed class Grade(val versao: String,
                   val qtdObrigatorias: Int,
                   val horasOptativas: Int,
                   val horasEletivas: Int,
                   val horasComplementares: Int) {

    companion object {
        fun versao(v: String) = if (v == Grade2008.versao) Grade2008 else Grade2023
    }

    object Grade2008: Grade("2008/1", 39, 480, 240, 0)
    object Grade2023: Grade("2023/2", 36, 600, 120, 90)
}