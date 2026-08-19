package adapter.input.rest

import adapter.input.ui.observacoes
import adapter.input.ui.tableInscricoesAluno
import adapter.input.ui.tableTotalizacaoInscricoes
import adapter.input.ui.tableInscricoesDisciplina
import adapter.input.ui.tableInscricoesDisciplinaBSI
import adapter.input.ui.tableInscricoesIrregulares
import io.ktor.http.*
import io.ktor.server.html.respondHtml
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.format
import kotlinx.html.h1
import main.collator
import main.currentDateTime
import main.fileTimestampFormat
import model.Aluno
import model.AlunoRepository
import model.InscricaoRepository
import model.RepositoryFactory
import model.TotalizacaoInscricao
import model.TotalizacaoInscricaoRepository
import model.TurmaRepository
import model.trancados

const val INSCRICOES_ROUTE = "/inscricoes"
const val DOWNLOAD_INSCRICOES_ROUTE = "/inscricoes/download"
const val INSCRICOES_IRREGULARES_ROUTE = "/inscricoes/irregulares"
const val DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE = "/inscricoes/irregulares/download"

fun Route.inscricoesRoutes() {

    get(INSCRICOES_ROUTE) {
        val sorting = call.request.queryParameters["sort"]?: ""

        call.respondHTML(status = HttpStatusCode.OK) {
            tableTotalizacaoInscricoes(findTotalizacaoInscricoes(sorting), sorting)
        }
    }

    get(DOWNLOAD_INSCRICOES_ROUTE) {
        val sorting = call.request.queryParameters["sort"]?: ""

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"totalizacao-inscricoes ${currentDateTime().format(fileTimestampFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Turma;Código;Nome;Solicitadas;Aceitas;Falta de vagas;Falta de pré-req;Canceladas\n" +
                    findTotalizacaoInscricoes(sorting).joinToString(separator = "\n") { it.toCsv() }
        }
    }

    get("$INSCRICOES_ROUTE/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondBadRequest()

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null) {
            call.respondHTML(status = HttpStatusCode.OK) {
                tableInscricoesAluno(aluno)
            }
        }
        else
            call.respondHTML(HttpStatusCode.OK) {
                h1 { +"Aluno com matrícula $matricula não encontrado!"}
            }
    }

    get("$INSCRICOES_ROUTE/{codigo}/{turma}") {
        val codigoDisciplina = call.parameters["codigo"]?:""
        val codigoTurma = call.parameters["turma"]?:""

        val turma = RepositoryFactory.get(TurmaRepository::class).findByCode(codigoTurma)

        // Se for uma turma do BSI com inscritos, vai ser encontrada no repositório de turmas
        // Se não for do BSI ou se não tiver inscritos, busca as inscrições diretamente no
        // repositório de inscrições pela turma/disciplina
        if (turma != null) {
            val disciplina = turma.getDisciplina(codigoDisciplina) ?: return@get call.respondBadRequest()

            val inscricoes = turma.inscricoes(disciplina).sortedBy { it.nomeAluno }

            call.respondHTML(status = HttpStatusCode.OK) {
                tableInscricoesDisciplinaBSI(inscricoes, turma, disciplina)
            }
        }
        else {
            val inscricoes = RepositoryFactory.get(InscricaoRepository::class).findByCodeTurmaDisciplina(codigoTurma, codigoDisciplina)

            call.respondHTML(status = HttpStatusCode.OK) {
                tableInscricoesDisciplina(inscricoes, codigoTurma, codigoDisciplina)
            }
        }
    }

    get(INSCRICOES_IRREGULARES_ROUTE) {
        call.respondHTML(status = HttpStatusCode.OK) {
            tableInscricoesIrregulares(findAlunosIrregulares())
        }
    }

    get(DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE) {

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"inscricoes-irregulares ${currentDateTime().format(fileTimestampFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Nome;Versao;E-mail;Matriculado;Trancamentos;Observacoes\n" +
            findAlunosIrregulares().joinToString(separator = "\n") { aluno ->
                "${aluno.toCsv()};${aluno.historico.trancados.joinToString(" / ")};${aluno.observacoes.joinToString(" / ")}"
            }
        }
    }
}

private fun findTotalizacaoInscricoes(sorting: String) : List<TotalizacaoInscricao> {
    val comparator = getInscricaoSortingByValue(sorting).comparator

    return RepositoryFactory.get(TotalizacaoInscricaoRepository::class).findTotalizacoes().sortedWith(comparator)
}

private fun findAlunosIrregulares() =
    RepositoryFactory.get(AlunoRepository::class)
        .findInscricoesIrregulares()
        .filter { it.estaIrregular }
        .sortedWith { a1, a2 ->
            if (a1.itensMatriculados.size == a2.itensMatriculados.size)
                collator.compare(a1.nome, a2.nome)
            else
                a1.itensMatriculados.size - a2.itensMatriculados.size
        }

private fun TotalizacaoInscricao.toCsv() = "${this.turma};${this.codigo};${this.nome};${this.solicitados};${this.aceitos};${this.faltaVagas};${this.faltaPreRequisito};${this.cancelados}"

private fun Aluno.toCsv() = "${this.matricula};${this.nome};${this.versao};${this.email};${this.itensMatriculados.size}"