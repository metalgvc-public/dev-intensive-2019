package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnit {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.format(pattern:String = "HH:mm:ss dd.MM.yy", locale:String = "ru"): String {
    val sDateFromat = SimpleDateFormat(pattern, Locale(locale))
    return sDateFromat.format(this)
}

fun Date.add(value: Int, units: TimeUnit = TimeUnit.SECOND): Date {

    this.time += when (units) {
        TimeUnit.SECOND -> value * SECOND
        TimeUnit.MINUTE -> value * MINUTE
        TimeUnit.HOUR -> value * HOUR
        TimeUnit.DAY -> value * DAY
    }

    return this
}

/**
 * 0с - 1с "только что"
 * 1с - 45с "несколько секунд назад"
 * 45с - 75с "минуту назад"
 * 75с - 45мин "N минут назад"
 * 45мин - 75мин "час назад"
 * 75мин 22ч "N часов назад"
 * 22ч - 26ч "день назад"
 * 26ч - 360д "N дней назад"
 * >360д "более года назад"
 */
fun Date.humanizeDiff(date: Date = Date()): String {
    val diffSeconds:Int = ((date.getTime() - this.getTime()) / SECOND).toInt()

    return when (diffSeconds) {
        in 0..1 -> "только что"
        in 1..45 -> "несколько секунд назад"
        in 45..75 -> "минуту назад"
        in 75..(45*60) -> {
            val minutes:Int = (diffSeconds / 60).toInt()

            if (minutes in 10..20) {
                return "$minutes минут назад"
            } else {
                return when (minutes.toString().takeLast(1).toInt()) {
                    1 -> "$minutes минуту назад"
                    in 2..4 -> "$minutes минуты назад"
                    else -> "$minutes минут назад"
                }
            }
        }
        in (45*60)..(75*60) -> "час назад"
        in (75*60)..(22*60*60) -> {
            val hours:Int = (diffSeconds / 60 / 60).toInt()

            if (hours in 10..20) {
                return "$hours часов назад"
            } else {
                return when (hours.toString().takeLast(1).toInt()) {
                    1 -> "$hours час назад"
                    in 2..4 -> "$hours часа назад"
                    else -> "$hours часов назад"
                }
            }
        }
        in (22*60*60)..(26*60*60) -> "день назад"
        in (26*60*60)..(360*24*60*60) -> {
            val days:Int = (diffSeconds / 60 / 60 / 24).toInt()

            if (days in 10..20) {
                return "$days дней назад"
            } else {
                return when (days.toString().takeLast(1).toInt()) {
                    1 -> "$days день назад"
                    in 2..4 -> "$days дня назад"
                    else -> "$days дней назад"
                }
            }
        }

        else -> "более года назад"
    }
}

