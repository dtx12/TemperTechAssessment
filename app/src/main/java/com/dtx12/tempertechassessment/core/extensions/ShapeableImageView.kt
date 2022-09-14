package com.dtx12.tempertechassessment.core.extensions

import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

fun ShapeableImageView.applyRoundedCorners(radius: Float) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setAllCorners(
            CornerFamily.ROUNDED,
            radius
        )
        .build()

}