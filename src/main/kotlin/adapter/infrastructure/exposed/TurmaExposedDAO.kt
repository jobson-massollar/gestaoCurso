package adapter.infrastructure.exposed

import model.DisciplinaDTO
import model.TurmaDisciplinaDTO
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDAO.ITurmaDAO

class TurmaExposedDAO: ITurmaDAO {
    override fun findAll(): List<TurmaDisciplinaDTO> =
        transaction {
            Turmas.selectAll().map {
                createDTO(it)
            }.toList()
        }
    }

private fun createDTO(row: ResultRow) =
    TurmaDisciplinaDTO(
        null,
        row[Turmas.codigoTurma].uppercase(),
        row[Turmas.inscritosTurma],
        DisciplinaDTO(null,
            row[Turmas.versao],
            row[Turmas.codigoDisciplina],
            row[Turmas.nomeDisciplina],
            row[Turmas.periodo],
            row[Turmas.creditos],
            row[Turmas.horas],
            row[Turmas.tipo],
            row[Turmas.inscritosDisciplina])
    )