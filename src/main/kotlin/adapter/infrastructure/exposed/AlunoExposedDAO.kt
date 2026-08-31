package adapter.infrastructure.exposed

import model.AlunoDTO
import model.Disciplina
import model.Inscricao
import model.ItemDiario
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.lowerCase
import org.jetbrains.exposed.v1.core.not
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDAO.IAlunoDAO

class AlunoExposedDAO: IAlunoDAO {
//    override fun insert(dto: AlunoDTO) {
//        transaction {
//            Alunos.insert {
//                it[Alunos.id] = dto.id!!
//                it[matricula] = dto.matricula
//                it[nome] = dto.nome
//                it[sexo] = dto.sexo
//                it[dataNasc] = dto.dataNasc
//                it[versao] = dto.versao
//                it[ingresso] = dto.ingresso
//                it[evasao] = dto.evasao
//                it[dataEvasao] = dto.dataEvasao
//                it[logradouro] = dto.logradouro
//                it[numero] = dto.numero
//                it[complemento] = dto.complemento
//                it[bairro] = dto.bairro
//                it[cidade] = dto.cidade
//                it[cep] = dto.cep
//                it[telefone1] = dto.telefone1
//                it[telefone2] = dto.telefone2
//                it[email] = dto.email
//            }
//        }
//    }

//    override fun update(dto: AlunoDTO) {
//        transaction {
//            Alunos.update({ Alunos.id eq dto.id!! }) {
//                it[matricula] = dto.matricula
//                it[nome] = dto.nome
//                it[sexo] = dto.sexo
//                it[dataNasc] = dto.dataNasc
//                it[versao] = dto.versao
//                it[ingresso] = dto.ingresso
//                it[evasao] = dto.evasao
//                it[dataEvasao] = dto.dataEvasao
//                it[logradouro] = dto.logradouro
//                it[numero] = dto.numero
//                it[complemento] = dto.complemento
//                it[bairro] = dto.bairro
//                it[cidade] = dto.cidade
//                it[cep] = dto.cep
//                it[telefone1] = dto.telefone1
//                it[telefone2] = dto.telefone2
//                it[email] = dto.email
//            }
//        }
//    }

//    override fun delete(dto: AlunoDTO) {
//        transaction {
//            Alunos.deleteWhere { Alunos.id eq dto.id!! }
//        }
//    }

    override fun findAll(search: String) = findWithSearch(Alunos, search)

    override fun findAtivos(search: String) = findWithSearch(AlunosAtivos, search)

    override fun findByMatricula(matricula: String): AlunoDTO? =
        transaction {
            Alunos
                .selectAll()
                .where { Alunos.matricula eq matricula }
                .map {
                    createDTO(Alunos, it)
                }.firstOrNull()
        }

    override fun findInscricoesIrregulares(): List<AlunoDTO> {
        // Recupera as matrículas que tem 3 ou mais inscrições aceitas
        // Esses alunos estão REGULARES!
        val qtdInscricoes = Inscricoes.matricula.count()
        val inscricoesValidas =
            Inscricoes
                .select(Inscricoes.matricula)
                .where { Inscricoes.situacao eq 2 }
                .groupBy(Inscricoes.matricula)
                .having { qtdInscricoes greater 2 }
                .alias("inscricoesValidas")

        // Recupera todos os alunos ativos que não estão REGULARES,
        // excluindo os ingressantes. Esses são candidatos a sere
        // IRREGULARES
        val inscricoesIrregulares =
            AlunosAtivos
                .join(
                inscricoesValidas,
                JoinType.LEFT) {
                    AlunosAtivos.matricula eq inscricoesValidas[Inscricoes.matricula]
                }
                .selectAll()
                .where { (inscricoesValidas[Inscricoes.matricula]).isNull() and (not (AlunosAtivos.matricula like "20262210%")) }

        return transaction {
            inscricoesIrregulares
                .map {
                    createDTO(AlunosAtivos, it)
                }.toList()
        }
    }

    override fun findByDiario(diario: ItemDiario): AlunoDTO? =
        transaction {
            AlunosAtivos
                .selectAll()
                .where { AlunosAtivos.matricula eq diario.matricula }
                .map {
                    createDTO(AlunosAtivos, it)
                }.firstOrNull()
        }

    override fun findByInscricao(inscricao: Inscricao): AlunoDTO? =
        transaction {
            AlunosAtivos
                .selectAll()
                .where { AlunosAtivos.matricula eq inscricao.matricula }
                .map {
                    createDTO(AlunosAtivos, it)
                }.firstOrNull()
        }

    override fun findByDisciplina(disciplina: Disciplina): List<AlunoDTO> =
        transaction {
            AlunosAtivos
                .join(ItensDiario, JoinType.INNER) {
                    AlunosAtivos.matricula eq ItensDiario.matricula
                }
                .select(AlunosAtivos.columns)
                .where { ItensDiario.codigo eq disciplina.codigo }
                .map {
                    createDTO(AlunosAtivos, it)
                }.toList()
        }

    override fun findPodemCursar(disciplina: Disciplina): List<AlunoDTO> =
        transaction {
            AlunosAtivos
                .join(AlunosAptosDisciplinas, JoinType.INNER) {
                    AlunosAtivos.matricula eq AlunosAptosDisciplinas.matricula
                }
                .select(AlunosAtivos.columns)
                .where { AlunosAptosDisciplinas.codigo eq disciplina.codigo }
                .map {
                    createDTO(AlunosAtivos, it)
                }.toList()
        }

    override fun findBySituacaoInscricao(disciplina: Disciplina, situacao: Int): List<AlunoDTO> =
        transaction {
            AlunosAtivos
                .join(SituacaoFinalInscricoes, JoinType.INNER) {
                    AlunosAtivos.matricula eq SituacaoFinalInscricoes.matricula
                }
                .select(AlunosAtivos.columns)
                .where {
                    (SituacaoFinalInscricoes.codigo eq disciplina.codigo) and
                    (SituacaoFinalInscricoes.situacao eq situacao)
                }
                .map {
                    createDTO(AlunosAtivos, it)
                }.toList()
        }

    private fun findWithSearch(table: AlunosBase, search: String): List<AlunoDTO> {
        val query = table.selectAll()
        if (search.isNotBlank()) {
            if (search[0].isDigit())
                query.where { table.matricula like "${search}%" }
            else
                query.where { table.nome.lowerCase() like "%$search%" }
        }

        return transaction {
            query.map {
                createDTO(table, it)
            }.toList()
        }
    }

    private fun createDTO(alunos: AlunosBase, row: ResultRow): AlunoDTO =
        AlunoDTO(
        row[alunos.id],
        row[alunos.matricula],
        row[alunos.nome],
        row[alunos.sexo],
        row[alunos.dataNascimento],
        row[alunos.versao],
        row[alunos.ingresso],
        row[alunos.logradouro],
        row[alunos.numero],
        row[alunos.complemento],
        row[alunos.bairro],
        row[alunos.cidade],
        row[alunos.cep],
        row[alunos.telefone1],
        row[alunos.telefone2],
        row[alunos.email],
        row[alunos.evasao],
        row[alunos.dataEvasao],
        row[alunos.trancamentos],
        row[alunos.prazoExtensao]
    )

//    override fun deleteAll() {
//        transaction {
//            Alunos.deleteAll()
//        }
//    }
//
//    override fun batchInsert(dtos: List<AlunoDTO>) {
//        transaction {
//            Alunos.batchInsert(
//                data = dtos,
//                shouldReturnGeneratedValues = false)
//                {dto ->
//                    this[Alunos.id] = dto.id!!
//                    this[matricula] = dto.matricula
//                    this[nome] = dto.nome
//                    this[sexo] = dto.sexo
//                    this[dataNasc] = dto.dataNasc
//                    this[versao] = dto.versao
//                    this[ingresso] = dto.ingresso
//                    this[evasao] = dto.evasao
//                    this[dataEvasao] = dto.dataEvasao
//                    this[logradouro] = dto.logradouro
//                    this[numero] = dto.numero
//                    this[complemento] = dto.complemento
//                    this[bairro] = dto.bairro
//                    this[cidade] = dto.cidade
//                    this[cep] = dto.cep
//                    this[telefone1] = dto.telefone1
//                    this[telefone2] = dto.telefone2
//                    this[email] = dto.email
//                }
//        }
//    }
}