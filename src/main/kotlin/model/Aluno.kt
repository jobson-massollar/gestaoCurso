package model

import kotlinx.datetime.LocalDate
import kotlin.math.max
import kotlin.math.min

class Aluno private constructor(val matricula: String, val nome: String, val sexo: Char, val dataNascimento: LocalDate?, val versao: String, val ingresso: String, val logradouro: String, val numero: String, val complemento: String, val bairro: String, val cidade: String, val cep: String, val telefone1: String, val telefone2: String, val email: String, val evasao: String, val dataEvasao: LocalDate?, val trancamentos: Int, val prazoExtensao: Int): Entity() {

    val grade = Grade.versao(versao)
    val periodoInicial = Periodo(matricula.take(4).toInt(), matricula[4].code - 48)
    val periodosPandemia = if (periodoInicial > FIM_PANDEMIA) 0 else min(6, FIM_PANDEMIA - periodoInicial)
    val periodoFinal = periodoInicial + periodosPandemia + 17 // 8 + 4 + 4 trancamentos
    val periodoLimite = periodoInicial + (11 + periodosPandemia + prazoExtensao + trancamentos)
    val isAtivo = dataEvasao == null && evasao.take(3) != "ABA"

    val historico by lazy {
        RepositoryFactory.get(ItemHistoricoRepository::class).findByMatricula(this)
    }

    val itensAprovados by lazy {
        historico.filter { it.isAprovado }
    }

    val itensMatriculados by lazy {
        historico.filter { it.isMatriculado }
    }

    val itensReprovados by lazy {
        historico.filter { it.isReprovado }
    }

    val disciplinasObrigatoriasFaltantes by lazy {
        RepositoryFactory.get(DisciplinaRepository::class).findObrigatoriasFaltantes(this)
    }

    val disciplinasObrigatoriasAprovadas by lazy {
        itensAprovados.obrigatorias.map { it.disciplina }
    }

    val disciplinasObrigatoriasACursar by lazy {
        (disciplinasObrigatoriasFaltantes - disciplinasObrigatoriasMatriculadas.toSet())
            .filter { disciplinasObrigatoriasAprovadas.containsAll(it.preRequisitos) }
    }

    val disciplinasObrigatoriasMatriculadas by lazy {
        itensMatriculados.obrigatorias.map { it.disciplina }
    }

    val estaFormado by lazy {
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

    val ehFormando by lazy {
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

    val horasOptativasFaltantes by lazy {
        val horasOptativas = itensAprovados.optativas.sumOf { it.horas }
        val horasOptativasMatr = itensMatriculados.optativas.sumOf { it.horas }
        val eletivas = itensAprovados.eletivas + itensMatriculados.eletivas

        if (grade is Grade.Grade2008)
            max(grade.horasOptativas - (horasOptativas + horasOptativasMatr), 0)
        else
            max(grade.horasOptativas - (horasOptativas + horasOptativasMatr + eletivas.eletivasAproveitadas.horasEletivasAproveitadas), 0)
    }

    val horasEletivasFaltantes by lazy {
        if (grade is Grade.Grade2008) {
            max(grade.horasEletivas - ((itensAprovados.eletivas + itensMatriculados.eletivas).sumOf { it.horas }), 0)
        }
        else
            0
    }

    val estaTrancado by lazy {
        val cursadas = historico.cursadas(Periodo.ATUAL)

        cursadas.size == 1 && cursadas[0].codigo == "TRT0001"
    }

    val estaIrregular by lazy {
        if (itensMatriculados.size >= 3 || estaTrancado || estaFormado)
            return@lazy false

        // Se não está matriculado em todas as obrigatórias possíveis, então está irregular
        if (itensAprovados.obrigatorias.size + itensMatriculados.obrigatorias.size < grade.qtdObrigatorias) {
            disciplinasObrigatoriasACursar.isNotEmpty()
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

    val jubiladoPorAbandono by lazy {
        itensMatriculados.cursadas(Periodo.ATUAL).isEmpty() && ! estaTrancado
    }

    val jubiladoPorPrazo by lazy {
        Periodo.ATUAL > periodoLimite && ! estaFormado
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
    //override fun toString() = "[id=$id matricula=$matricula, nome=$nome, sexo=$sexo, dtNasc=$dataNasc, versao=$versao, logradouro = $logradouro, numero = $numero, complemento = $complemento, bairro = $bairro, cidade = $cidade, cep = $cep, telefone1 = $telefone1, telefone2 = $telefone2, email = $email, ingresso=$ingresso, evasao=$evasao, dtEvasao=$dataEvasao)"
}