package services.domain.persistence

interface IAlunoDAO: IDAO<AlunoDTO> {
    fun findAtivos(search: String = ""): List<AlunoDTO>
    fun findByMatricula(matricula: String): AlunoDTO?
}