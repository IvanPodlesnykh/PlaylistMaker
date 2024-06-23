package com.ivanpodlesnykh.playlistmaker.utils

import android.content.Context
import android.util.TypedValue

class UtilFunctions {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}