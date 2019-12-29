package com.bourdi_bay.wordoftheday

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat

class DailyWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_word)

        val b = intent.extras
        val dailyWord = b?.getParcelable<DailyWord>("dailyWord")

        val titleView = findViewById<TextView>(R.id.title)
        titleView.text = dailyWord?.word

        val definitionView = findViewById<TextView>(R.id.definition)
        definitionView.text = dailyWord?.definition

        val examplesView = findViewById<TextView>(R.id.examples)
        val examplesBulleted =
            dailyWord?.examples?.joinToString(separator = "<br/><br/>") { it -> "=> $it" }
        if (examplesBulleted != null)
            examplesView.text =
                HtmlCompat.fromHtml(examplesBulleted, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val oftenUsedView = findViewById<TextView>(R.id.oftenUsed)
        val oftenUsedBulleted =
            dailyWord?.oftenUsed?.joinToString(separator = "<br/><br/>") { it -> "=> $it" }
        if (oftenUsedBulleted != null)
            oftenUsedView.text =
                HtmlCompat.fromHtml(oftenUsedBulleted, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
