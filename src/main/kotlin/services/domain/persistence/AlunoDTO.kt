package services.domain.persistence

import kotlinx.datetime.LocalDate
import model.Aluno
import model.EntityDTO
import kotlin.uuid.Uuid

class AlunoDTO(id: Uuid?, val matricula: String, val nome: String, val sexo: Char, val dataNasc: LocalDate?, val versao: String, val ingresso: String, val evasao: String, val dataEvasao: LocalDate?, val logradouro: String, val numero: String, val complemento: String, val bairro: String, val cidade: String, val cep: String, val telefone1: String, val telefone2: String, val email: String): EntityDTO(id) {
    companion object {
        fun fromEntity(a: Aluno) = AlunoDTO(a.id, a.matricula, a.nome, a.sexo, a.dataNasc, a.versao, a.ingresso, a.evasao, a.dataEvasao, a.logradouro, a.numero, a.complemento, a.bairro, a.cidade, a.cep, a.telefone1, a.telefone2, a.email)
        fun toEntity(dto: AlunoDTO) = Aluno.of(dto.matricula, dto.nome, dto.sexo, dto.dataNasc, dto.versao, dto.ingresso, dto.evasao, dto.dataEvasao, dto.logradouro, dto.numero, dto.complemento, dto.bairro, dto.cidade, dto.cep, dto.telefone1, dto.telefone2, dto.email)
    }
}