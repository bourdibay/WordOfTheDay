package com.bourdi_bay.wordoftheday.scrapers

import android.os.AsyncTask
import android.util.Log
import com.bourdi_bay.wordoftheday.DailyWord

class DailyWordScraperTask(private val postExecute: (DailyWord?) -> Unit) :
    AsyncTask<String, Void, DailyWord?>() {

    override fun doInBackground(vararg params: String): DailyWord? {
        val url = params[0]

        val startTimer = System.currentTimeMillis()

        val dailyWordScraper = DailyWordScraper()
        val dailyWord = dailyWordScraper.loadFromUrl(url)

        val endTimer = System.currentTimeMillis()
        val elapsedSeconds = (endTimer - startTimer) / 1000.0
        Log.i("BENCH", "Elapsed time (in sec) for $url (${dailyWord?.word}): $elapsedSeconds")

        return dailyWord
    }

    override fun onPostExecute(dailyWord: DailyWord?) {
        postExecute(dailyWord)
    }
}
