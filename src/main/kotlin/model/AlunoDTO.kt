package model

import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

class AlunoDTO(id: Uuid?,
               val matricula: String,
               val nome: String,
               val sexo: Char,
               val dataNascimento: LocalDate?,
               val versao: String,
               val ingresso: String,
               val logradouro: String,
               val numero: String,
               val complemento: String,
               val bairro: String,
               val cidade: String,
               val cep: String,
               val telefone1: String,
               val telefone2: String,
               val email: String,
               val evasao: String,
               val dataEvasao: LocalDate?,
               val trancamentos: Int,
               val prazoExtensao: Int): EntityDTO<Aluno>(id) {

    override fun toEntity() = Aluno.of(matricula,
        nome,
        sexo,
        dataNascimento,
        versao,
        ingresso,
        logradouro,
        numero,
        complemento,
        bairro,
        cidade,
        cep,
        telefone1,
        telefone2,
        email,
        evasao,
        dataEvasao,
        trancamentos,
        prazoExtensao)
}