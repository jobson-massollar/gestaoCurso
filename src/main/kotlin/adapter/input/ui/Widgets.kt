package adapter.input.ui

import io.ktor.htmx.HxSwap
import io.ktor.htmx.html.hx
import kotlinx.html.FlowContent
import kotlinx.html.HTMLTag
import kotlinx.html.button
import kotlinx.html.radioInput
import kotlin.text.toInt

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

fun FlowContent.smallButton(label: String, url: String, container: String, disabled: Boolean = false) {
    button(classes = "btn btn-ghost btn-xs") {
        attributes.hx {
            get = url
            target = container
            swap = HxSwap.innerHtml
            indicator = "#loading-spinner"
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