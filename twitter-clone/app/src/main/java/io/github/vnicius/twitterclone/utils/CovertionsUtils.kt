package io.github.vnicius.twitterclone.utils

import android.content.Context
import android.util.DisplayMetrics



class CovertionsUtils {
    companion object {
        fun convertDpToPixels(value: Int, context: Context) =
            value * (context.getResources().getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}