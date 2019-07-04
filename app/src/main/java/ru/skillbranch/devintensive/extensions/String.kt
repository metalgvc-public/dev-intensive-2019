package ru.skillbranch.devintensive.extensions

fun String.truncate(len:Int = 16): String {
    val str:String = this.trim()
    if (str.length <= len) {
        return str
    }
    return str.substring(0, len).trim() +"..."
}

fun String.stripHtml():String {

    var str = this.replace("<[^>]*>".toRegex(), "")
    str = str.replace("&[^;]*;".toRegex(), "")
    str = str.replace("\\s+".toRegex(), " ")

    return str
}