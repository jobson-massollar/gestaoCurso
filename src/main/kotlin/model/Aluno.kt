package model

import kotlinx.datetime.LocalDate
import kotlin.math.min

class Aluno private constructor(val matricula: String, val nome: String, val sexo: Char, val dataNasc: LocalDate?, val versao: String, val ingresso: String, val evasao: String, val dataEvasao: LocalDate?, val logradouro: String = "", val numero: String = "", val complemento: String = "", val bairro: String = "", val cidade: String = "", val cep: String = "", val telefone1: String = "", val telefone2: String = "", val email: String = ""): Entity() {

    val semestreInicial = Semestre(matricula.take(4).toInt(), matricula[4].code - 48)
    val semestreFinal = semestreInicial + (if (semestreInicial >= FIM_PANDEMIA) 17 else min(6, FIM_PANDEMIA - semestreInicial) + 17)

    companion object {
        fun of(matricula: String, nome: String, sexo: Char, dataNasc: LocalDate?, versao: String, ingresso: String, evasao: String, dataEvasao: LocalDate?, logradouro: String = "", numero: String = "", complemento: String = "", bairro: String = "", cidade: String = "", cep: String = "", telefone1: String = "", telefone2: String = "", email: String = "") =
            Aluno(matricula, nome, sexo, dataNasc, versao, ingresso, evasao, dataEvasao, logradouro, numero, complemento, bairro, cidade, cep, telefone1, telefone2, email)
    }

    val isAtivo = dataEvasao == null && evasao.take(3) != "ABA"

    val migrou = versao == "2023/2" && matricula.take(4) < "20232"

    //override fun toString() = "[id=$id matricula=$matricula, nome=$nome, sexo=$sexo, dtNasc=$dataNasc, versao=$versao, logradouro = $logradouro, numero = $numero, complemento = $complemento, bairro = $bairro, cidade = $cidade, cep = $cep, telefone1 = $telefone1, telefone2 = $telefone2, email = $email, ingresso=$ingresso, evasao=$evasao, dtEvasao=$dataEvasao)"
}