package io.github.vnicius.twitterclone.utils

import android.support.v4.text.HtmlCompat
import android.text.Spanned
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Summarize the number to a fancy [String]
 */
fun Int.summarizeNumber(): String {

    // for millions
    if (this >= 1000000) {
        val firstPart: Int = this / 1000000
        val secondPart: Int = (this - firstPart * 1000000)

        if (secondPart < 100000) {
            return "${firstPart}M"
        }

        return "$firstPart.${secondPart / 100000}M"
    } else if (this >= 1000) {
        // for thousands
        val fistPart: Int = this / 1000
        val secondPart = (this - fistPart * 1000)

        if (fistPart in 10..99) {
            if (secondPart < 100) {
                return "${fistPart}K"
            }

            return "$fistPart.${secondPart / 100}K"
        } else if (fistPart >= 100) {
            return "${fistPart}K"
        }

        // check the hundred part
        if (secondPart < 10) {
            return "$fistPart.00$secondPart"
        }
        if (secondPart < 100) {
            return "$fistPart.0$secondPart"
        }

        return "$fistPart.$secondPart"
    }
    return this.toString()
}

/**
 * Summarize the number to counts items
 */
fun Int.summarizeCountNumber() = if (this == 0) "" else this.summarizeNumber()

/**
 * Highlight citations and urls in text
 */
fun String.highlightClickable(): Spanned {

    // regex for citations and hashtags
    val reCitation = Regex("""([@#]\w+)""")

    // regex to links
    val reLinks = Regex("""(http\S+)""")

    // regex to e-mails
    val reEMails = Regex("""(\w+@\S+)""")

    // parse the text
    var textParsed = reEMails.replace(this, """<font color="#1DA1F2">$1</font>""")
    textParsed = reCitation.replace(textParsed, """<font color="#1DA1F2">$1</font>""")
    textParsed = reLinks.replace(textParsed, """<font color="#1DA1F2">$1</font>""")

    return HtmlCompat.fromHtml(textParsed, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

/**
 * Parse the time values
 */
fun Long.parseTweetTime(): String {

    // get the difference time
    val diffDate = Date().time - this

    // get the days value
    val days = TimeUnit.MILLISECONDS.toDays(diffDate)

    if (days > 30) {
        // parse the time to a string date
        val actualDate = Calendar.getInstance()
        val valueDate = Calendar.getInstance()
        var formater: SimpleDateFormat
        valueDate.time = Date(this)

        formater = if (valueDate.get(Calendar.YEAR) != actualDate.get(Calendar.YEAR)) {
            SimpleDateFormat("MM dd yy")
        } else {
            SimpleDateFormat("MM dd")
        }

        return formater.format(valueDate.time)

    } else if (days > 0) {
        return "${days}d"
    }

    // get the hours
    val hours = TimeUnit.MILLISECONDS.toHours(diffDate)

    if (hours > 0) {
        return "${hours}h"
    }

    // get the minutes
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffDate)

    return if (minutes > 0) {
        "${minutes}m"
    } else {
        // get the seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffDate)
        "${seconds}s"
    }
}

