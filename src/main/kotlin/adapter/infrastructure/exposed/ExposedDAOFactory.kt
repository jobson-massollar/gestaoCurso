package adapter.infrastructure.exposed

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO
import services.domain.persistence.IDAOFactory

object ExposedDAOFactory : IDAOFactory {
//    override fun <T : IDAO<*,*>> get(c: KClass<in T>): T =
//        when (c) {
////            IDisciplinaDAO::class -> DisciplinaExposedDAO() as T
//            IAlunoDAO::class -> AlunoExposedDAO() as T
//            IDAO.IItemHistoricoDAO::class -> ItemHistoricoExposedDAO() as T
////            IInscricaoDAO::class -> InscricaoExposedDAO() as T
////            IItemDiarioDAO::class -> ItemDiarioExposedDAO() as T
//            else -> throw IllegalArgumentException("Invalid DAO class " + c.java.name)
//        }

    override fun <T : IDAO<*, *>> getDAO(t: DAOFactory.Type): T =
        when (t) {
            DAOFactory.Type.DISCIPLINA -> DisciplinaExposedDAO() as T
            DAOFactory.Type.ALUNO -> AlunoExposedDAO() as T
            DAOFactory.Type.HISTORICO -> ItemHistoricoExposedDAO() as T
            DAOFactory.Type.PRE_REQUISITO -> PreRequisitoExposedDAO() as T
            DAOFactory.Type.TURMA -> TurmaExposedDAO() as T
            DAOFactory.Type.DIARIO -> ItemDiarioExposedDAO() as T
            DAOFactory.Type.INSCRICAO -> InscricaoExposedDAO() as T
            DAOFactory.Type.TOTAL_INSCRICAO -> TotalizacaoInscricaoExposedDAO() as T
            DAOFactory.Type.LOG_IMPORTACAO-> LogImportacaoExposedDAO() as T
        }
}