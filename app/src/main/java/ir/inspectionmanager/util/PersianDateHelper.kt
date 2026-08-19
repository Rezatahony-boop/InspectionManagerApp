package ir.inspectionmanager.util

import com.aminography.primcalendar.civil.CivilCalendar
import java.util.Calendar

object PersianDateHelper {

    fun todayJalali(): String {
        val calendar = CivilCalendar()

        return String.format(
            "%04d/%02d/%02d",
            calendar.year,
            calendar.month + 1,
            calendar.dayOfMonth
        )
    }

    fun toJalali(calendar: Calendar): String {
        val civilCalendar = CivilCalendar(calendar.time)

        return String.format(
            "%04d/%02d/%02d",
            civilCalendar.year,
            civilCalendar.month + 1,
            civilCalendar.dayOfMonth
        )
    }

    fun jalaliToMillis(jalaliDate: String): Long {
        return try {
            val parts = jalaliDate.split("/")

            if (parts.size != 3) {
                return System.currentTimeMillis()
            }

            val year = parts[0].toIntOrNull()
                ?: return System.currentTimeMillis()

            val month = (parts[1].toIntOrNull() ?: 1) - 1
            val day = parts[2].toIntOrNull()
                ?: return System.currentTimeMillis()

            val civilCalendar = CivilCalendar(
                year,
                month,
                day
            )

            civilCalendar.timeInMillis

        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    fun getDayOfWeekPersian(jalaliDate: String): String {
        val milli = jalaliToMillis(jalaliDate)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milli

        val days = arrayOf(
            "شنبه",
            "یکشنبه",
            "دوشنبه",
            "سه‌شنبه",
            "چهارشنبه",
            "پنج‌شنبه",
            "جمعه"
        )

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        return when (dayOfWeek) {
            Calendar.SATURDAY -> "شنبه"
            Calendar.SUNDAY -> "یکشنبه"
            Calendar.MONDAY -> "دوشنبه"
            Calendar.TUESDAY -> "سه‌شنبه"
            Calendar.WEDNESDAY -> "چهارشنبه"
            Calendar.THURSDAY -> "پنج‌شنبه"
            Calendar.FRIDAY -> "جمعه"
            else -> "نامشخص"
        }
    }

    fun getWeekDates(jalaliDate: String): Pair<String, String> {
        val milli = jalaliToMillis(jalaliDate)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milli

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val daysToSaturday = when (dayOfWeek) {
            Calendar.SATURDAY -> 0
            Calendar.SUNDAY -> 1
            Calendar.MONDAY -> 2
            Calendar.TUESDAY -> 3
            Calendar.WEDNESDAY -> 4
            Calendar.THURSDAY -> 5
            Calendar.FRIDAY -> 6
            else -> 0
        }

        calendar.add(
            Calendar.DAY_OF_MONTH,
            -daysToSaturday
        )

        val startDate = toJalali(calendar)

        calendar.add(
            Calendar.DAY_OF_MONTH,
            6
        )

        val endDate = toJalali(calendar)

        return Pair(startDate, endDate)
    }

    fun getMonthDates(jalaliDate: String): Pair<String, String> {

        val parts = jalaliDate.split("/")

        if (parts.size != 3) {
            return Pair(
                "1400/01/01",
                "1400/01/31"
            )
        }

        val year = parts[0]
        val month = parts[1].toIntOrNull() ?: 1

        val startDate =
            "$year/${String.format("%02d", month)}/01"

        val lastDay = when (month) {
            1, 2, 3, 4, 5, 6 -> 31
            7, 8, 9, 10, 11 -> 30
            12 -> {
                // تشخیص سال کبیسه با استفاده از تبدیل تاریخ
                val nextYear = year.toIntOrNull()?.plus(1) ?: 1401

                try {
                    val endOfYear = CivilCalendar(
                        nextYear,
                        0,
                        1
                    )

                    val currentYearStart = CivilCalendar(
                        year.toInt(),
                        0,
                        1
                    )

                    val difference =
                        (endOfYear.timeInMillis -
                                currentYearStart.timeInMillis) /
                                (24L * 60L * 60L * 1000L)

                    if (difference >= 366) 30 else 29

                } catch (e: Exception) {
                    29
                }
            }

            else -> 30
        }

        val endDate =
            "$year/${String.format("%02d", month)}/$lastDay"

        return Pair(startDate, endDate)
    }
}
