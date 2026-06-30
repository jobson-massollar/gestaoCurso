package adapter.input.ui

import io.ktor.htmx.*
import io.ktor.htmx.html.*
import kotlinx.html.FlowContent
import kotlinx.html.HTMLTag
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.hr
import kotlinx.html.label
import kotlinx.html.radioInput
import kotlinx.html.span
import kotlinx.html.unsafe

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

fun FlowContent.title(title: String, downloadURL: String = "", block: FlowContent.() -> Unit = {}) {
    div(classes = "mb-4 p-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        //div(classes = "m-4") {
            div(classes = "flex") {
                label(classes = "text-lg font-semibold") { +title }
                if (downloadURL.isNotBlank()) {
                    label(classes = "ml-auto mr-4") {
                        downloadButton(downloadURL)
                    }
                }
            }
            hr(classes = "mb-2 border-base-content/50") {  }
            block()
        //}
    }
}

fun FlowContent.smallButton(label: String, url: String, container: String, disabled: Boolean = false) {
    button(classes = "ms-2 btn btn-xs btn-primary") {
        attributes.hx {
            get = url
            target = container
            swap = HxSwap.innerHtml
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
            swap = HxSwap.innerHtml
            indicator = "#loading-spinner"
            include = "closest form"
        }
        name = fieldName
        value = filterValue
        checked = currentFilter == filterValue
    }
}

fun FlowContent.downloadButton(url: String) {
    a(classes = "btn btn-ghost") {
        href = url
        unsafe { +DOWNLOAD_SVG }
    }
}