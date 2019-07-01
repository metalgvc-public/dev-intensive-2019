package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.format(pattern:String = "HH:mm:ss dd.MM.yy", locale:String = "ru"): String {
    val sDateFromat = SimpleDateFormat(pattern, Locale(locale))
    return sDateFromat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {

    this.time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
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

    fun past(diffSeconds: Int): String {
        return when (diffSeconds) {
            in 0..1 -> "только что"
            in 1..45 -> "несколько секунд назад"
            in 45..75 -> "минуту назад"
            in 75..(45*60) -> {
                val minutes:Int = (diffSeconds / 60).toInt()
                var _minutes:Int = minutes

                if (minutes > 100) {
                    _minutes = minutes.toString().substring(minutes.toString().length - 2, minutes.toString().length).toInt()
                }

                if (_minutes in 10..20) {
                    return "$minutes минут назад"
                } else {
                    return when (_minutes.toString().takeLast(1).toInt()) {
                        1 -> "$minutes минуту назад"
                        in 2..4 -> "$minutes минуты назад"
                        else -> "$minutes минут назад"
                    }
                }
            }
            in (45*60)..(75*60) -> "час назад"
            in (75*60)..(22*60*60) -> {
                val hours:Int = (diffSeconds / 60 / 60).toInt()
                var _hours:Int = hours

                if (hours > 100) {
                    _hours = hours.toString().substring(hours.toString().length - 2, hours.toString().length).toInt()
                }

                if (_hours in 10..20) {
                    return "$hours часов назад"
                } else {
                    return when (_hours.toString().takeLast(1).toInt()) {
                        1 -> "$hours час назад"
                        in 2..4 -> "$hours часа назад"
                        else -> "$hours часов назад"
                    }
                }
            }
            in (22*60*60)..(26*60*60) -> "день назад"
            in (26*60*60)..(360*24*60*60) -> {
                val days:Int = (diffSeconds / 60 / 60 / 24).toInt()
                var _days:Int = days

                if (days > 100) {
                    _days = days.toString().substring(days.toString().length - 2, days.toString().length).toInt()
                }

                if (_days in 10..20) {
                    return "$days дней назад"
                } else {
                    return when (_days.toString().takeLast(1).toInt()) {
                        1 -> "$days день назад"
                        in 2..4 -> "$days дня назад"
                        else -> "$days дней назад"
                    }
                }
            }

            else -> "более года назад"
        }
    }

    fun future(diffSeconds: Int): String {
        return when (diffSeconds) {
            in 0..1 -> "только что"
            in 1..45 -> "через несколько секунд"
            in 45..75 -> "через минуту"
            in 75..(45*60) -> {
                val minutes:Int = (diffSeconds / 60).toInt()
                var _minutes:Int = minutes

                if (minutes > 100) {
                    _minutes = minutes.toString().substring(minutes.toString().length - 2, minutes.toString().length).toInt()
                }

                if (_minutes in 10..20) {
                    return "через $minutes минут"
                } else {
                    return when (_minutes.toString().takeLast(1).toInt()) {
                        1 -> "через $minutes минуту"
                        in 2..4 -> "через $minutes минуты"
                        else -> "через $minutes минут"
                    }
                }
            }
            in (45*60)..(75*60) -> "через час"
            in (75*60)..(22*60*60) -> {
                val hours:Int = (diffSeconds / 60 / 60).toInt()
                var _hours:Int = hours;

                if (hours > 100) {
                    _hours = hours.toString().substring(hours.toString().length - 2, hours.toString().length).toInt()
                }

                if (_hours in 10..20) {
                    return "через $hours часов"
                } else {
                    return when (_hours.toString().takeLast(1).toInt()) {
                        1 -> "через $hours час"
                        in 2..4 -> "через $hours часа"
                        else -> "через $hours часов"
                    }
                }
            }
            in (22*60*60)..(26*60*60) -> "через день"
            in (26*60*60)..(360*24*60*60) -> {
                val days:Int = (diffSeconds / 60 / 60 / 24).toInt()
                var _days:Int = days

                if (days > 100) {
                    _days = days.toString().substring(days.toString().length - 2, days.toString().length).toInt()
                }

                if (_days in 10..20) {
                    return "через $days дней"
                } else {
                    return when (_days.toString().takeLast(1).toInt()) {
                        1 -> "через $days день"
                        in 2..4 -> "через $days дня"
                        else -> "через $days дней"
                    }
                }
            }

            else -> "более чем через год"
        }
    }

    if (diffSeconds > 0) {
        return past(diffSeconds)
    } else {
        var _diffSeconds: Int = diffSeconds * -1
        return future(_diffSeconds)
    }
}

