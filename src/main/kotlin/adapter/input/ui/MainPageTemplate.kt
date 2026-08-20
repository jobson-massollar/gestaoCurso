package adapter.input.ui

import adapter.input.rest.ALUNOS_COLACAO_ROUTE
import adapter.input.rest.ALUNOS_EXTENSAO_ROUTE
import adapter.input.rest.ALUNOS_ROUTE
import adapter.input.rest.INSCRICOES_IRREGULARES_ROUTE
import adapter.input.rest.INSCRICOES_ROUTE
import adapter.input.rest.SITUACAO_IRREGULAR_ROUTE
import adapter.input.rest.TURMAS_ROUTE
import io.ktor.htmx.html.*
import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.html.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.html.*
import main.UITimestampFormat
import main.fileTimestampFormat

class MainPageTemplate(private val version: String, private val ultimaImportacao: LocalDateTime?): Template<HTML> {

    val pageBody = TemplatePlaceholder<PageBodyTemplate>()

    override fun HTML.apply() {
        lang = "pt-BR"
        attributes["data-theme"] = "nord"

        head {
            title { +"Gestão do BSI" }
            meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
            meta(name="charset", content = "utf-8")
            link(rel = "icon", href = "/static/img/favicon.png", type = "image/png")
            link(href="https://cdn.jsdelivr.net/npm/daisyui@5.7.16", rel="stylesheet", type = "text/css")
            link(href="https://cdn.jsdelivr.net/npm/daisyui@5.7.16/themes.css", rel="stylesheet", type = "text/css")
            script(src = "https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4") {}
            script(src = "https://cdn.jsdelivr.net/npm/htmx.org@4.0.0-beta6", crossorigin = ScriptCrossorigin.anonymous) {}
            script(src = "https://cdn.jsdelivr.net/npm/htmx.org@4.0.0-beta6/dist/ext/hx-download.js") {}
            script(src = "https://cdn.jsdelivr.net/npm/sweetalert2@11") {}
        }

        body {
            insert(PageBodyTemplate(version, ultimaImportacao), pageBody)
        }
    }
}

class PageBodyTemplate(private val version: String, private val ultimaImportacao: LocalDateTime?): Template<FlowContent> {

    val mainContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        val dtHoraImportacao = ultimaImportacao?.format(UITimestampFormat)

        // Spinner de espera das requisições ao backend
        // Fica oculto e só é apresentado durante as requisições
        div(classes = "htmx-indicator fixed inset-0 flex items-center justify-center z-50") {
            id = "loading-spinner"
            img {
                src = "/static/img/spinning-circle-transparent.gif"//spinner-dots-rotate.svg"
                alt = "Carregando..."
            }
        }
        div(classes = "navbar bg-base-100 shadow-sm") {
            // Menu principal da app
            div(classes = "navbar-start") {
                div(classes = "dropdown") {
                    div(classes = "btn btn-ghost btn-circle") {
                        role = "button"
                        tabIndex = 10
                        unsafe { +MENU_SVG }
                    }
                    ul(classes = "menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-100 p-2 shadow") {
                        tabIndex = -1

                        li {
                            mainMenuItem(ALUNOS_ROUTE, "Alunos")
                        }
                        li {
                            mainMenuItem(ALUNOS_COLACAO_ROUTE, "Alunos aptos para Colação de Grau")
                        }
                        li {
                            mainMenuItem(ALUNOS_EXTENSAO_ROUTE, "Alunos com 11 ou mais Períodos")
                        }
                        li {
                            mainMenuItem(INSCRICOES_IRREGULARES_ROUTE, "Alunos com menos de 3 Inscrições no Período")
                        }
                        li {
                            mainMenuItem(SITUACAO_IRREGULAR_ROUTE, "Alunos em Situação Irregular de Matricula (Abandono e Prazo)")
                        }
                        li {
                            mainMenuItem(INSCRICOES_ROUTE, "Inscrições")
                        }
                        li {
                            mainMenuItem(TURMAS_ROUTE, "Turmas e Diários de Classe")
                        }
                        li {
                            mainMenuItem(TURMAS_ROUTE, "Disciplinas 2008/1")
                        }
                        li {
                            mainMenuItem(TURMAS_ROUTE, "Disciplinas 2023/2")
                        }
                        li {
                            a {
                                // Para fechar o menu e chamar a modal
                                onClick = "document.activeElement.blur();about_modal.showModal()"
                                +"Sobre"
                            }
                        }
                    }
                    // Janela modal "sobre"
                    dialog(classes = "modal") {
                        id = "about_modal"
                        div(classes = "modal-box") {
                            h3(classes = "text-lg font-bold") { +"Gestão do BSI" }
                            p(classes = "py-4") { +"Versão $version" }
                            p { +"Importação: $dtHoraImportacao" }
                            div(classes = "modal-action") {
                                form {
                                    attributes["method"] = "dialog"
                                    button(classes = "btn", type = ButtonType.submit) { +"Fechar" }
                                }
                            }
                        }
                    }
                }
            }

            // Título
            div(classes = "navbar-center") {
                div(classes="flex items-start") {
                    span(classes = "text-3xl font-bold") {
                        +"Gestão do BSI"
                    }
                    span(classes = "ml-4 text-sm") {
                        +"v${version}"
                    }
                }
            }

            // Checkbox de troca de tema claro/escuro
            div(classes = "navbar-end") {
                checkBoxInput(classes = "toggle theme-controller") {
                    value = "dark"
                }
            }
        }

        div(classes = "text-right text-sm mt-2 mr-5") {
            +(dtHoraImportacao?:"")
        }

        // Área central da página onde os conteúdos serão renderizados
        div(classes = "w-dwv h-fit mx-5 mt-2") {
            id = "main-container"

            insert(mainContent)
        }
    }
}

private fun LI.mainMenuItem(url: String, label: String) {
    a {
        attributes.hx {
            get = url
            target = "#main-container"
            swap = "innerHTML"
            indicator = "#loading-spinner"
            pushUrl = "true"

        }
        // Para fechar o menu
        onClick = "document.activeElement.blur()"
        +label
    }
}