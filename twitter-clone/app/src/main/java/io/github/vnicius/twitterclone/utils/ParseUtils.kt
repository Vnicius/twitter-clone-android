package io.github.vnicius.twitterclone.utils

import android.annotation.SuppressLint
import androidx.core.text.HtmlCompat
import android.text.Spanned
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Summarize the number to a fancy [String]
 */
fun Int.summarizeNumber(): String {
    val firstPart: Int
    val secondPart: Int

    // for millions
    if (this >= 1000000) {
        firstPart = this / 1000000
        secondPart = (this - firstPart * 1000000)

        if (secondPart < 100000) return "${firstPart}M"

        return "$firstPart.${secondPart / 100000}M"
    } else if (this >= 1000) {
        // for thousands
        firstPart = this / 1000
        secondPart = (this - firstPart * 1000)

        if (firstPart in 10..99) {
            if (secondPart < 100) return "${firstPart}K"

            return "$firstPart.${secondPart / 100}K"
        } else if (firstPart >= 100) {
            return "${firstPart}K"
        }

        // check the hundred part
        if (secondPart < 10) return "$firstPart.00$secondPart"

        if (secondPart < 100) return "$firstPart.0$secondPart"

        return "$firstPart.$secondPart"
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

    val reCitation = Regex("""([@#]\w+)""") // regex for citations and hashtags
    val reLinks = Regex("""(http\S+)""")    // regex to links
    val reEMails = Regex("""(\w+@\S+)""")   // regex to e-mails
    var textParsed = reEMails.replace(this, """<font color="#1DA1F2">$1</font>""")

    textParsed = reCitation.replace(textParsed, """<font color="#1DA1F2">$1</font>""")
    textParsed = reLinks.replace(textParsed, """<font color="#1DA1F2">$1</font>""")

    return HtmlCompat.fromHtml(textParsed, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

/**
 * Parse the time values
 */
@SuppressLint("SimpleDateFormat")
fun Long.parseTweetTime(): String {

    val diffDate = Date().time - this   // get the difference time
    val days = TimeUnit.MILLISECONDS.toDays(diffDate)   // get the days value
    val hours = TimeUnit.MILLISECONDS.toHours(diffDate)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffDate)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffDate)
    val actualDate = Calendar.getInstance()
    val valueDate = Calendar.getInstance()
    val formatter: SimpleDateFormat

    if (days > 30) {
        valueDate.time = Date(this)

        formatter = if (valueDate.get(Calendar.YEAR) != actualDate.get(Calendar.YEAR)) {
            SimpleDateFormat("MM dd yy")
        } else {
            SimpleDateFormat("MM dd")
        }

        return formatter.format(valueDate.time)
    } else if (days > 0) {
        return "${days}d"
    }

    if (hours > 0) return "${hours}h"

    return if (minutes > 0) "${minutes}m" else "${seconds}s"
}

fun String.toFileName(): String {
    return this.toLowerCase().replace(" ", "_")
}