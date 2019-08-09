package io.github.vnicius.twitterclone.utils


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

                return "${firstPart}.${secondPart / 100000}M"
            }
            else if(number >= 1000) {
                // for hundred numbers
                val fistPart: Int = number / 1000

                if(fistPart in 10..99) {
                    val secondPart = (number - fistPart * 1000)

                    if(secondPart < 100) {
                        return "${fistPart}K"
                    }

                    return "$fistPart.${secondPart / 100}K"
                } else if (fistPart >= 100) {
                    return "${fistPart}K"
                }

                return "$fistPart.${number - fistPart * 1000}"
            }
            return number.toString()
        }

        fun parseCountNumber(number: Int) = if (number == 0) "" else parseNumber(number)

        fun parseTime(value: Long): String {
            return value.toString()
        }
    }
}