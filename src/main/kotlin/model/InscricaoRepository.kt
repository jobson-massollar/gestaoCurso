package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class InscricaoRepository : Repository<Inscricao, IDAO.IInscricaoDAO, InscricaoDTO>() {

    override val dao: IDAO.IInscricaoDAO = DAOFactory.getDAO(DAOFactory.Type.INSCRICAO)

    // fun findByDisciplina(codigo: String, turma: String) = createEntityList(dao.findByDisciplina(codigo, turma))

    fun findByAluno(aluno: Aluno) = createEntityList(dao.findByAluno(aluno))

    fun findByTurmaDisciplina(turma: Turma, disciplina: Disciplina) = createEntityList(dao.findByTurmaDisciplina(turma, disciplina))
}