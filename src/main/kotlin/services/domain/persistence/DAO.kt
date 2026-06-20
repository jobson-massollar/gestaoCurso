package services.domain.persistence

import model.Aluno
import model.Disciplina
import model.Entity
import model.EntityDTO
import model.ItemHistorico
import kotlin.reflect.KClass

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
        fun findAll(matricula: String): List<ItemHistoricoDTO>
        fun findAprovados(matricula: String): List<ItemHistoricoDTO>
        fun findMatriculados(matricula: String): List<ItemHistoricoDTO>
    }

    interface IDisciplinaDAO: IDAO<Disciplina, DisciplinaDTO> {
        fun findAll(): List<DisciplinaDTO>
        fun findObrigatoriasFaltantes(matricula: String): List<DisciplinaDTO>
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
//        INSCRICAO,
//        DIARIO
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