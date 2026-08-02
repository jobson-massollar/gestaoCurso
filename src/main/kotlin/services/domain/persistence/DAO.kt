package services.domain.persistence

import model.*

sealed interface IDAO<E: Entity, T: EntityDTO<E>> {
//    fun insert(dto: T)
//    fun update(dto: T)
//    fun delete(dto: T)
//    fun findAll(search: String = ""): List<T>
//    fun deleteAll()
//    fun batchInsert(dtos: List<T>)

    interface IAlunoDAO: IDAO<Aluno, AlunoDTO> {
        fun findAll(search: String = ""): List<AlunoDTO>
        fun findAtivos(search: String = ""): List<AlunoDTO>
        fun findByMatricula(matricula: String): AlunoDTO?
    }

    interface IItemHistoricoDAO: IDAO<ItemHistorico, ItemHistoricoDTO> {
        fun findAll(): List<ItemHistoricoDTO>
        fun findByAluno(aluno: Aluno): List<ItemHistoricoDTO>
//        fun findAprovados(matricula: String): List<ItemHistoricoDTO>
//        fun findMatriculados(matricula: String): List<ItemHistoricoDTO>
    }

    interface IDisciplinaDAO: IDAO<Disciplina, DisciplinaDTO> {
        fun findAll(): List<DisciplinaDTO>
        fun findObrigatoriasFaltantes(aluno: Aluno): List<DisciplinaDTO>
        fun findPreRequisitos(disciplina: Disciplina): List<DisciplinaDTO>
        fun findByItemHistorico(itemHistorico: ItemHistorico): DisciplinaDTO
        fun findByCode(versao: String, codigo: String): DisciplinaDTO
    }

    interface IPreRequisitoDAO: IDAO<PreRequisito, PreRequisitoDTO>

    interface ITurmaDAO: IDAO<Turma, TurmaDisciplinaDTO> {
        fun findAll(): List<TurmaDisciplinaDTO>
    }

    interface IItemDiarioDAO: IDAO<ItemDiario, ItemDiarioDTO> {
        fun findAll(disciplina: Disciplina): List<ItemDiarioDTO>
    }
}

interface IDAOFactory {
    //fun <T:IDAO<*, *>> get(c: KClass<in T>): T
    fun <T : IDAO<*,*>> getDAO(t: DAOFactory.Type): T
}

object DAOFactory: IDAOFactory {

    enum class Type {
        DISCIPLINA,
        ALUNO,
        HISTORICO,
        PRE_REQUISITO,
        TURMA,
        DIARIO
//        INSCRICAO
    }

    private var factory: IDAOFactory? = null

    fun register(factory: IDAOFactory) {
        this.factory = factory
    }

//    override fun <T : IDAO<*>> get(c: KClass<in T>): T =
//        factory?.get(c) ?: throw NullPointerException("DAO Factory not set")

    override fun <T : IDAO<*,*>> getDAO(t: Type): T  =
        factory?.getDAO(t) ?: throw NullPointerException("DAO Factory not set")
}