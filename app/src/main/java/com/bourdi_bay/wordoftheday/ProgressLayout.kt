package com.bourdi_bay.wordoftheday

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class ProgressLayout(val view: View?) {

    private val progressLayout = view?.findViewById<LinearLayout>(R.id.progress_layout)
    private val progressBar = progressLayout?.findViewById<ProgressBar>(R.id.progress_bar)
    private val progressText = progressLayout?.findViewById<TextView>(R.id.progress_text)

    fun initializeProgressBar(max: Int) {
        progressBar?.progress = 0
        progressBar?.max = max
        if (max > 0)
            progressLayout?.visibility = View.VISIBLE
        progressText?.text = "0/$max"
    }

    fun updateProgressBar(visible: Boolean) {
        if (progressBar != null) {
            progressBar.progress++
        }
        progressText?.text = "${progressBar?.progress}/${progressBar?.max}"
        if (visible) {
            progressLayout?.visibility = View.GONE
        }
    }
}