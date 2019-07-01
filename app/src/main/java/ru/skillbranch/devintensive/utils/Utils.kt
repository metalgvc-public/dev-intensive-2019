package ru.skillbranch.devintensive.utils

object Utils {

    val translitMap: Map<String, String> = mapOf("а" to "a", "б" to "b", "в" to "v", "г" to "g", "д" to "d", "е" to "e",
        "ё" to "e", "ж" to "zh", "з" to "z", "и" to "i", "й" to "i", "к" to "k", "л" to "l", "м" to "m", "н" to "n",
        "о" to "o", "п" to "p", "р" to "r", "с" to "s", "т" to "t", "у" to "u", "ф" to "f", "х" to "h", "ц" to "c",
        "ч" to "ch", "ш" to "sh", "щ" to "sh'", "ъ" to "", "ы" to "i", "ь" to "", "э" to "e", "ю" to "yu", "я" to "ya")

    fun parseFullName(fullName:String?): Pair<String?, String?> {
        val parts:List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        firstName = if(firstName.isNullOrBlank()) null else firstName;
        lastName = if(lastName.isNullOrBlank()) null else lastName;

        return Pair(firstName, lastName)
    }

    fun toInitials(firstName:String?, lastName:String?): String? {
        var first:String = ""
        var second:String = ""

        if (!firstName.isNullOrBlank() || !lastName.isNullOrBlank()) {
            first = if (firstName.isNullOrBlank()) "" else firstName.substring(0, 1).capitalize()
            second = if (lastName.isNullOrBlank()) "" else lastName.substring(0, 1).capitalize()
        }

        val initials:String? = "${first}${second}"

        return if (initials.isNullOrBlank()) null else initials
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var result: String = payload
        val delimiter: String = " "

        for ((from, to) in this.translitMap) {
            result = result.replace(from, to, true)
        }

        val words = result.split(delimiter).toMutableList()

        result = ""
        for(word in words){
            result += word.capitalize() +divider
        }
        result = result.removeSuffix(divider)

        return result
    }
}