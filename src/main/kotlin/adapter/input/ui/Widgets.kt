package adapter.input.ui

import io.ktor.htmx.html.*
import kotlinx.html.*

fun Float.format(digits: Int) = "%.${digits}f".format(this)

var HTMLTag.role: String
    get() = attributes["role"] ?: ""
    set(value) {
        attributes["role"] = value
    }

var HTMLTag.tabIndex: Int
    get() = attributes["tabindex"]?.toInt() ?: 0
    set(value) {
        attributes["tabindex"] = value.toString()
    }

fun FlowContent.title(title: String, downloadURL: String = "", backButton: Boolean = false, block: FlowContent.() -> Unit = {}) {
    div(classes = "mb-4 p-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        div(classes = "flex items-center") {
            if (backButton) {
                button(classes = "btn btn-ghost") {
                    onClick = "history.back()"
                    unsafe { +BACK_SVG }
                }
            }
            label(classes = "text-lg font-semibold") { +title }
            if (downloadURL.isNotBlank()) {
                label(classes = "ml-auto") {
                    downloadButton(downloadURL)
                }
            }
        }
        hr(classes = "mb-2 border-base-content/50") {  }
        block()
    }
}

fun FlowContent.smallButton(label: String, url: String, container: String, disabled: Boolean = false) {
    button(classes = "ms-2 btn btn-xs btn-primary") {
        attributes.hx {
            get = url
            target = container
            swap = "innerHTML show:top showTarget:body"
            indicator = "#loading-spinner"
            pushUrl = "true"
        }
        this.disabled = disabled
        +label
    }
}

fun FlowContent.radioButton(fieldName: String, url: String, container: String, currentFilter: String, filterValue: String) {
    radioInput(classes = "radio radio-sm ml-4") {
        attributes.hx {
            get = url
            target = container
            swap = "innerHTML"
            indicator = "#loading-spinner"
            include = "closest form"
            replaceUrl = "true"
        }
        name = fieldName
        value = filterValue
        checked = currentFilter == filterValue
    }
}

fun FlowOrPhrasingContent.sortingButton(url: String, formName: String, sorting: String, container: String, svg: String) {
    button(classes = "btn btn-ghost btn-xs ml-2 pl-px pr-px base-content") {
        attributes.hx {
            get = url
            target = container
            swap = "innerHTML"
            indicator = "#loading-spinner"
            include = "closest form"
            replaceUrl = "true"
        }
        onClick = "document.$formName.sort.value = '$sorting'"
        unsafe { +svg }
    }
}

fun FlowOrPhrasingContent.sortingButtons(url: String, formName: String, fieldAsc: String, fieldDesc: String) {
    sortingButton(url, formName, fieldAsc, "#main-container", AZ_SORT_SVG)
    sortingButton(url, formName, fieldDesc, "#main-container",ZA_SORT_SVG)
}

fun FlowContent.downloadButton(url: String) {
    a(classes = "btn btn-ghost") {
        attributes.hx {
            get = url
            swap = "none"
            indicator = "#loading-spinner"
        }
        unsafe { +DOWNLOAD_SVG }
    }
}