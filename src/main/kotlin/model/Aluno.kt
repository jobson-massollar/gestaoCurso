package model

import kotlinx.datetime.LocalDate
import kotlin.math.max
import kotlin.math.min

val String.ehAlunoBSI: Boolean
    get() = this.length == 11 && this.substring(5, 8) == "210"

sealed class StatusPeriodo(val periodo: Periodo, val isAcimaLimite: Boolean, val isPandemia: Boolean = false, val numero: Int = 0) {
    class Matriculado(periodo: Periodo, isAcimaLimite: Boolean, isPandemia: Boolean, numero: Int): StatusPeriodo(periodo, isAcimaLimite, isPandemia, numero)
    class ACursar(periodo: Periodo, isAcimaLimite: Boolean, numero: Int): StatusPeriodo(periodo, isAcimaLimite, numero = numero)
    class Trancado(periodo: Periodo, isAcimaLimite: Boolean, isPandemia: Boolean): StatusPeriodo(periodo, isAcimaLimite, isPandemia)
    class NaoMatriculado(periodo: Periodo, isAcimaLimite: Boolean, isPandemia: Boolean, numero: Int): StatusPeriodo(periodo, isAcimaLimite, isPandemia, numero)
}

class Aluno private constructor(val matricula: String,
                                val nome: String,
                                val sexo: Char,
                                val dataNascimento: LocalDate?,
                                val versao: String,
                                val ingresso: String,
                                val logradouro: String,
                                val numero: String,
                                val complemento: String,
                                val bairro: String,
                                val cidade: String,
                                val cep: String,
                                val telefone1: String,
                                val telefone2: String,
                                val email: String,
                                val evasao: String,
                                val dataEvasao: LocalDate?,
                                val trancamentos: Int,
                                val prazoExtensao: Int): Entity() {

    val grade = Grade.versao(versao)
    val periodoInicial = Periodo(matricula.take(4).toInt(), matricula[4].code - 48)
    val periodosPandemia: Int = if (periodoInicial > FIM_PANDEMIA) 0 else min(6, FIM_PANDEMIA - periodoInicial)
    val periodoFinal: Periodo = periodoInicial + periodosPandemia + 17 // 8 + 4 + 4 trancamentos
    val periodoLimite: Periodo = periodoInicial + (11 + periodosPandemia + prazoExtensao + trancamentos)
    val estaAtivo: Boolean = dataEvasao == null && evasao.take(3) != "ABA"

    private val cacheStatusPeriodo = mutableMapOf<Periodo, List<StatusPeriodo>>()

    fun statusPeriodos(periodoAtual: Periodo): List<StatusPeriodo>  =
        cacheStatusPeriodo.getOrPut(periodoAtual) {
            val periodoPandemia = INICIO_PANDEMIA..FIM_PANDEMIA
            val periodos = mutableListOf<StatusPeriodo>()
            var i = 1
            for (p in periodoInicial..periodoFinal) {
                val historicoPeriodo = historico.cursadas(p)
                val isPandemia = p in periodoPandemia
                val isAcimaLimite = p > periodoLimite
                val status = when {
                    historicoPeriodo.isEmpty() ->
                        if (p > periodoAtual)
                            StatusPeriodo.ACursar(p, isAcimaLimite, i++)
                        else
                            StatusPeriodo.NaoMatriculado(p, isAcimaLimite, isPandemia, if (isPandemia) 0 else i++)

                    historicoPeriodo[0].isTrancamento ->
                        StatusPeriodo.Trancado(p, isAcimaLimite, isPandemia)

                    else ->
                        StatusPeriodo.Matriculado(p, isAcimaLimite, isPandemia, if (isPandemia) 0 else i++)
                }
                periodos.add(status)
            }
            periodos
        }

    val ultimoPeriodoCursado: StatusPeriodo by lazy {
        statusPeriodos(Periodo.ATUAL).filter { status ->
            status is StatusPeriodo.Matriculado || status is StatusPeriodo.NaoMatriculado
        }.maxBy { it.numero }
    }

    fun cursouPeloMenos(qtdPeriodos: Int): Boolean =
        statusPeriodos(Periodo.ATUAL).any { status ->
            status.numero >= qtdPeriodos
        }

    val historico: List<ItemHistorico> by lazy {
        RepositoryFactory.get(ItemHistoricoRepository::class).findByAluno(this)
    }

    val inscricoes: List<Inscricao> by lazy {
        RepositoryFactory.get(InscricaoRepository::class).findByAluno(this)
    }

    val itensAprovados: List<ItemHistorico> by lazy {
        historico.filter { it.isAprovado }
    }

    val itensMatriculados: List<ItemHistorico> by lazy {
        historico.filter { it.isMatriculado && it.periodo == Periodo.ATUAL.semestre && it.ano == Periodo.ATUAL.ano }
    }

    val itensReprovados: List<ItemHistorico> by lazy {
        historico.filter { it.isReprovado }
    }

    val disciplinasObrigatoriasFaltantes: List<Disciplina> by lazy {
        RepositoryFactory.get(DisciplinaRepository::class).findObrigatoriasFaltantes(this)
    }

    val disciplinasObrigatoriasAprovadas: List<Disciplina> by lazy {
        itensAprovados.obrigatorias.map { it.disciplina }
    }

    val disciplinasObrigatoriasACursar: List<Disciplina> by lazy {
        (disciplinasObrigatoriasFaltantes - disciplinasObrigatoriasMatriculadas.toSet())
            .filter { disciplinasObrigatoriasAprovadas.containsAll(it.preRequisitos) }
    }

    val disciplinasObrigatoriasMatriculadas: List<Disciplina> by lazy {
        itensMatriculados.obrigatorias.map { it.disciplina }
    }

    val estaFormado: Boolean by lazy {
        when {
            itensAprovados.obrigatorias.size < grade.qtdObrigatorias -> false

            itensAprovados.complementares.sumOf { it.horas } < grade.horasComplementares -> false

            else -> {
                val horasOptativas = itensAprovados.optativas.sumOf { it.horas }
                val eletivas = itensAprovados.eletivas

                if (grade is Grade.Grade2008)
                    horasOptativas >= grade.horasOptativas && eletivas.sumOf { it.horas } >= grade.horasEletivas
                else
                    horasOptativas + eletivas.eletivasAproveitadas.horasEletivasAproveitadas >= grade.horasOptativas
            }
        }
    }

    val ehFormando: Boolean by lazy {
        if (itensAprovados.obrigatorias.size + itensMatriculados.obrigatorias.size < grade.qtdObrigatorias)
            return@lazy false

        val horasOptativas = itensAprovados.optativas.sumOf { it.horas }
        val horasOptativasMatr = itensMatriculados.optativas.sumOf { it.horas }
        val eletivas = itensAprovados.eletivas + itensMatriculados.eletivas

        if (grade is Grade.Grade2008)
            horasOptativas + horasOptativasMatr >= grade.horasOptativas && eletivas.sumOf { it.horas } >= grade.horasEletivas
        else
            horasOptativas + horasOptativasMatr + eletivas.eletivasAproveitadas.horasEletivasAproveitadas >= grade.horasOptativas
    }

    val horasOptativasFaltantes: Int by lazy {
        val horasOptativas = itensAprovados.optativas.sumOf { it.horas }
        val horasOptativasMatr = itensMatriculados.optativas.sumOf { it.horas }
        val eletivas = itensAprovados.eletivas + itensMatriculados.eletivas

        if (grade is Grade.Grade2008)
            max(grade.horasOptativas - (horasOptativas + horasOptativasMatr), 0)
        else
            max(grade.horasOptativas - (horasOptativas + horasOptativasMatr + eletivas.eletivasAproveitadas.horasEletivasAproveitadas), 0)
    }

    val horasEletivasFaltantes: Int by lazy {
        if (grade is Grade.Grade2008) {
            max(grade.horasEletivas - ((itensAprovados.eletivas + itensMatriculados.eletivas).sumOf { it.horas }), 0)
        }
        else
            0
    }

    val estaTrancado: Boolean by lazy {
        val cursadas = historico.cursadas(Periodo.ATUAL)

        cursadas.size == 1 && cursadas[0].codigo == "TRT0001"
    }

    val estaIrregular: Boolean by lazy {
        if (itensMatriculados.cursadas(Periodo.ATUAL).size >= 3 || estaTrancado || estaFormado)
            return@lazy false

//        if (matricula == "20221210001") {
//           println(itensAprovados.obrigatorias.size)
//            println(itensMatriculados.obrigatorias.size)
//            println(grade.qtdObrigatorias)
//            println(disciplinasObrigatoriasACursar.size)
//        }

        // Se não está matriculado em todas as obrigatórias possíveis, então está irregular
        if (itensAprovados.obrigatorias.size + itensMatriculados.obrigatorias.size < grade.qtdObrigatorias &&
            disciplinasObrigatoriasACursar.isNotEmpty()) {
            return@lazy true
        }

        // Se não está fazendo todas as horas de optativas/eletivas que anda restam, então está irregular
        val horasOptativas = itensAprovados.optativas.sumOf { it.horas }
        val horasOptativasMatr = itensMatriculados.optativas.sumOf { it.horas }
        val eletivas = itensAprovados.eletivas + itensMatriculados.eletivas

        if (grade is Grade.Grade2008)
            horasOptativas + horasOptativasMatr < grade.horasOptativas || eletivas.sumOf { it.horas } < grade.horasEletivas
        else
            horasOptativas + horasOptativasMatr + eletivas.eletivasAproveitadas.horasEletivasAproveitadas < grade.horasOptativas
    }

    val irregularPorAbandono: Boolean by lazy {
//        if (matricula == "20192210019") {
//            println(estaTrancado)
//            println(estaFormado)
//            println(Periodo.ATUAL)
//            println(itensMatriculados.size)
//            println(itensMatriculados.first())
//            println(itensMatriculados.cursadas(Periodo.ATUAL).size)
//        }
        ! estaTrancado && ! estaFormado && itensMatriculados.cursadas(Periodo.ATUAL).isEmpty()
    }

    val irregularPorPrazo: Boolean by lazy {
        ! estaTrancado && ! estaFormado && Periodo.ATUAL > periodoLimite
    }

    override fun equals(other: Any?): Boolean =
        if (other is Aluno)
            matricula == other.matricula
        else
            false

    override fun hashCode() = matricula.hashCode()

    companion object {
        fun of(matricula: String, nome: String, sexo: Char, dataNascimento: LocalDate?, versao: String, ingresso: String, logradouro: String, numero: String, complemento: String, bairro: String, cidade: String, cep: String, telefone1: String, telefone2: String, email: String, evasao: String, dataEvasao: LocalDate?, trancamentos: Int, prazoExtensao: Int) =
            Aluno(matricula, nome, sexo, dataNascimento, versao, ingresso, logradouro, numero, complemento, bairro, cidade, cep, telefone1, telefone2, email, evasao, dataEvasao, trancamentos, prazoExtensao)
    }
}