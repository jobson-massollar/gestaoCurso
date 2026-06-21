package model

import kotlinx.datetime.LocalDate
import kotlin.math.min

class Aluno private constructor(val matricula: String, val nome: String, val sexo: Char, val dataNascimento: LocalDate?, val versao: String, val ingresso: String, val logradouro: String, val numero: String, val complemento: String, val bairro: String, val cidade: String, val cep: String, val telefone1: String, val telefone2: String, val email: String, val evasao: String, val dataEvasao: LocalDate?, val trancamentos: Int, val prazoExtensao: Int): Entity() {

    val periodoInicial = Periodo(matricula.take(4).toInt(), matricula[4].code - 48)
    val periodosPandemia = if (periodoInicial > FIM_PANDEMIA) 0 else min(6, FIM_PANDEMIA - periodoInicial)
    val periodoFinal = periodoInicial + periodosPandemia + 17 // 8 + 4 + 4 trancamentos
    val periodoLimite = periodoInicial + (11 + periodosPandemia + prazoExtensao + trancamentos)
    val isAtivo = dataEvasao == null && evasao.take(3) != "ABA"
    //val migrou = versao == "2023/2" && matricula.take(4) < "20232"

    companion object {
        fun of(matricula: String, nome: String, sexo: Char, dataNascimento: LocalDate?, versao: String, ingresso: String, logradouro: String, numero: String, complemento: String, bairro: String, cidade: String, cep: String, telefone1: String, telefone2: String, email: String, evasao: String, dataEvasao: LocalDate?, trancamentos: Int, prazoExtensao: Int) =
            Aluno(matricula, nome, sexo, dataNascimento, versao, ingresso, logradouro, numero, complemento, bairro, cidade, cep, telefone1, telefone2, email, evasao, dataEvasao, trancamentos, prazoExtensao)
    }

    //override fun toString() = "[id=$id matricula=$matricula, nome=$nome, sexo=$sexo, dtNasc=$dataNasc, versao=$versao, logradouro = $logradouro, numero = $numero, complemento = $complemento, bairro = $bairro, cidade = $cidade, cep = $cep, telefone1 = $telefone1, telefone2 = $telefone2, email = $email, ingresso=$ingresso, evasao=$evasao, dtEvasao=$dataEvasao)"
}