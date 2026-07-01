package adapter.input.ui

import io.ktor.htmx.*
import io.ktor.htmx.html.*
import io.ktor.server.html.*
import kotlinx.html.*

class MainPageTemplate: Template<HTML> {

    val content = Placeholder<FlowContent>()

    override fun HTML.apply() {
        lang = "pt-BR"
        attributes["data-theme"] = "nord"

        head {
            title { +"Gestão do BSI" }
            meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
            meta(name="charset", content = "utf-8")
            link(href="https://cdn.jsdelivr.net/npm/daisyui@5.6", rel="stylesheet", type = "text/css")
            link(href="https://cdn.jsdelivr.net/npm/daisyui@5.6/themes.css", rel="stylesheet", type = "text/css")
            script(src = "https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4") {}
            script(src = "https://unpkg.com/htmx.org@2.0.7", crossorigin = ScriptCrossorigin.anonymous) {}
            script(src = "https://cdn.jsdelivr.net/npm/sweetalert2@11") {}
            script(src = "/static/script/util.js") {}
        }

        body {
            onLoad = "main()"
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
                        ul(classes = "menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow") {
                            tabIndex = -1

                            li {
                                mainMenuItem("/alunos", "Alunos")
                            }
                            li {
                                mainMenuItem("/alunos/extensao", "Alunos com 11 ou mais períodos")
                            }
                            li {
                                mainMenuItem("/inscricoes/irregulares", "Inscrições irregulares")
                            }
                            li {
                                mainMenuItem("/jubilamentos", "Jubilamentos")
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
                                p(classes = "py-4") { +"Versão beta 0.1" }
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
                    span(classes = "text-xl font-bold") {
                        +"Gestão do BSI"
                    }
                }

                // Checkbox de troca de tema claro/escuro
                div(classes = "navbar-end") {
                    checkBoxInput(classes = "toggle theme-controller") {
                        value = "dark"
                    }
                }
            }

            // Área central da página onde os conteúdos serão renderizados
            div(classes = "w-dwv h-fit m-5") {
                id = "main-container"

                insert(content)
            }
        }
    }

    private fun LI.mainMenuItem(url: String, label: String) {
        a {
            attributes.hx {
                get = url
                target = "#main-container"
                swap = HxSwap.innerHtml
                indicator = "#loading-spinner"
            }
            // Para fechar o menu
            onClick = "document.activeElement.blur()"
            +label
        }
    }
}