package model

import services.application.AlunoFilter
import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class AlunoRepository: Repository<Aluno, IDAO.IAlunoDAO, AlunoDTO>() {

    override val dao: IDAO.IAlunoDAO = DAOFactory.getDAO(DAOFactory.Type.ALUNO)

    fun findByFilter(filter: AlunoFilter, search: String = "") =
        when (filter) {
            AlunoFilter.ALL -> createEntityList(dao.findAll(search))
            AlunoFilter.ACTIVE -> createEntityList(dao.findAtivos(search))
            AlunoFilter.GRADUATED -> createEntityList(dao.findAtivos(search)).filter { it.estaFormado }
            AlunoFilter.GRADUATING -> createEntityList(dao.findAtivos(search)).filter { it.ehFormando }
        }

    fun findByMatricula(matricula: String): Aluno? = createEntity(dao.findByMatricula(matricula) ?: return null)

    fun findInscricoesIrregulares(): List<Aluno> = createEntityList(dao.findInscricoesIrregulares())

    fun findByDiario(diario: ItemDiario): Aluno? = createEntity(dao.findByDiario(diario) ?: return null)

    fun findByInscricao(inscricao: Inscricao): Aluno? = createEntity(dao.findByInscricao(inscricao) ?: return null)

    fun findByDisciplina(disciplina: Disciplina): List<Aluno> = createEntityList(dao.findByDisciplina(disciplina))

    fun findPodemCursar(disciplina: Disciplina): List<Aluno> = createEntityList(dao.findPodemCursar(disciplina))

    fun findBySituacaoInscricao(disciplina: Disciplina, situacao: Int): List<Aluno> = createEntityList(dao.findBySituacaoInscricao(disciplina, situacao))
}