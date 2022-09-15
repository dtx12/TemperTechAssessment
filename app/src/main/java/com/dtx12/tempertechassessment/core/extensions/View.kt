package com.dtx12.tempertechassessment.core.extensions

import android.view.View

fun View.visible(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}