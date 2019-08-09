package io.github.vnicius.twitterclone.utils

import android.support.v4.content.res.ResourcesCompat
import android.support.v4.text.HtmlCompat
import android.text.Spanned
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.SimpleFormatter


class ParseUtils {
    companion object {
        fun parseNumber(number: Int): String {

            // for million numbers
            if (number >= 1000000) {
                val firstPart: Int = number / 1000000
                val secondPart: Int = (number - firstPart * 1000000)

                if(secondPart < 100000) {
                    return "${firstPart}M"
                }

                return "$firstPart.${secondPart / 100000}M"
            }
            else if(number >= 1000) {
                // for hundred numbers
                val fistPart: Int = number / 1000
                val secondPart = (number - fistPart * 1000)

                if(fistPart in 10..99) {
                    if(secondPart < 100) {
                        return "${fistPart}K"
                    }

                    return "$fistPart.${secondPart / 100}K"
                } else if (fistPart >= 100) {
                    return "${fistPart}K"
                }

                if(secondPart < 100) {
                    return "$fistPart.0$secondPart"
                }

                return "$fistPart.$secondPart"
            }
            return number.toString()
        }

        fun parseCountNumber(number: Int) = if (number == 0) "" else parseNumber(number)

        fun parseTweetText(text: String): Spanned {
            val reCitation = Regex("""([@#]\w+)""")
            val reLinks = Regex("""(http\S+)""")
            var textParsed = reCitation.replace(text, """<font color="#1DA1F2">$1</font>""")
            textParsed = reLinks.replace(textParsed, """<font color="#1DA1F2">$1</font>""")

            return HtmlCompat.fromHtml(textParsed, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

        fun parseTime(value: Long): String {
            val diffDate = Date().time - value
            val days = TimeUnit.MILLISECONDS.toDays(diffDate)

            if (days > 30) {
                val actualDate = Calendar.getInstance()
                val valueDate = Calendar.getInstance()
                var formater: SimpleDateFormat
                valueDate.time = Date(value)

                formater = if(valueDate.get(Calendar.YEAR) != actualDate.get(Calendar.YEAR)) {
                    SimpleDateFormat("MM dd yy")
                } else {
                    SimpleDateFormat("MM dd")
                }

                return formater.format(valueDate.time)

            } else if(days > 0) {
                return "${days}d"
            }

            val hours = TimeUnit.MILLISECONDS.toHours(diffDate)

            if(hours > 0) {
                return "${hours}h"
            }

            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffDate)

            return if (minutes > 0) {
                "${minutes}m"
            } else{
                val seconds = TimeUnit.MILLISECONDS.toSeconds(diffDate)
                "${seconds}s"
            }
        }
    }
}