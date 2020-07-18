package rocks.wintren.rency.util.extensions

fun String.capitalizeWords(): String = split(" ")
    .joinToString(" ") { it.capitalize() }