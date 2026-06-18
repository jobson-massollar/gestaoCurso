package services.domain.persistence

import model.Aluno
import model.AlunoFilter

class AlunoRepository: Repository<Aluno, IAlunoDAO, AlunoDTO>() {

    override val dao: IAlunoDAO = DAOFactory.getDAO(DAOFactory.Type.ALUNO)

//    override fun createDTO(e: Aluno) = AlunoDTO.fromEntity(e)

    override fun createEntity(dto: AlunoDTO) = AlunoDTO.toEntity(dto)

    fun findByFilter(filter: AlunoFilter, search: String) =
        when (filter) {
            AlunoFilter.ALL -> toEntityList(dao.findAll(search))
            AlunoFilter.ACTIVE -> toEntityList(dao.findAtivos(search))
        }

    fun findByMatricula(matricula: String): Aluno? = createEntity(dao.findByMatricula(matricula) ?: return null)
}