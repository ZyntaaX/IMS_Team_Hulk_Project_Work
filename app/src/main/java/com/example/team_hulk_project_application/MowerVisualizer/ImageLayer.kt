package com.example.team_hulk_project_application.MowerVisualizer

import android.graphics.Bitmap

data class ImageLayer (
        var bitmap: Bitmap,
        var isVisible: Boolean,
        var keyWord: ImageLayerKeyword?
) {
    override fun toString() = keyWord.toString()
}