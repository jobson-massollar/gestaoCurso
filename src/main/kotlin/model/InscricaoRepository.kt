package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class InscricaoRepository : Repository<Inscricao, IDAO.IInscricaoDAO, InscricaoDTO>() {

    override val dao: IDAO.IInscricaoDAO = DAOFactory.getDAO(DAOFactory.Type.INSCRICAO)

    fun findByCodeTurmaDisciplina(codigoTurma: String, codigoDisciplina: String) = createEntityList(dao.findByCodeTurmaDisciplina(codigoTurma, codigoDisciplina))

    fun findByAluno(aluno: Aluno) = createEntityList(dao.findByAluno(aluno))

    fun findByTurmaDisciplina(turma: Turma, disciplina: Disciplina) = createEntityList(dao.findByTurmaDisciplina(turma, disciplina))
}