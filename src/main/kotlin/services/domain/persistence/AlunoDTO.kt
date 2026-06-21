package services.domain.persistence

import kotlinx.datetime.LocalDate
import model.Aluno
import model.EntityDTO
import kotlin.uuid.Uuid

class AlunoDTO(id: Uuid?, val matricula: String, val nome: String, val sexo: Char, val dataNascimento: LocalDate?, val versao: String, val ingresso: String, val logradouro: String, val numero: String, val complemento: String, val bairro: String, val cidade: String, val cep: String, val telefone1: String, val telefone2: String, val email: String, val evasao: String, val dataEvasao: LocalDate?, val trancamentos: Int, val prazoExtensao: Int): EntityDTO<Aluno>(id) {
    companion object {
        fun fromEntity(a: Aluno) = AlunoDTO(a.id, a.matricula, a.nome, a.sexo, a.dataNascimento, a.versao, a.ingresso, a.evasao, a.logradouro, a.numero, a.complemento, a.bairro, a.cidade, a.cep, a.telefone1, a.telefone2, a.email, a.dataEvasao, a.trancamentos, a.prazoExtensao)
    }

    override fun toEntity() = Aluno.of(matricula, nome, sexo, dataNascimento, versao, ingresso, logradouro, numero, complemento, bairro, cidade, cep, telefone1, telefone2, email, evasao, dataEvasao, trancamentos, prazoExtensao)
}