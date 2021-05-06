package com.example.team_hulk_project_application.MowerVisualizer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

val bitmapGenerator = BitmapGenerator()

class BitmapGenerator {
    fun createBitmap(layers: MutableList<ImageLayer>, send:(bitmap: Bitmap, dstRect: Rect, canvas: Canvas) -> Unit) {
        val bitmap = layers[0].bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val dstRect = Rect(0, 0, layers[0].bitmap.width, layers[0].bitmap.height)
        val canvas = Canvas(bitmap)

        for (layer in layers) {
            if (layer.isVisible) {
                canvas.drawBitmap(layer.bitmap, null, dstRect, null)
            }
        }

        send(bitmap, dstRect, canvas)
    }
}