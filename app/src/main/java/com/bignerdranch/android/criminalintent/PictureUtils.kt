package com.bignerdranch.android.criminalintent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.roundToInt

fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
    // Read in the dimensions of the image on disk
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    // Figure out how much to scale down by
    val sampleSize = if (srcHeight <= destHeight && srcWidth <= destWidth) {
        1    // 1 means 1 pixel for every horizontal pixel in the original image
        // likewise, 2 means 2 pixel for every horizontal pixels (shrink size to 1/4 for 2-D image)
    } else {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        minOf(heightScale, widthScale).roundToInt()
    }

    // Read in and create final bitmap
    return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
        inSampleSize = sampleSize
    })
}