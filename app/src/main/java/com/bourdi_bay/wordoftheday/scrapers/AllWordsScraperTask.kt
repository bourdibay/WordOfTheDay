package com.bourdi_bay.wordoftheday.scrapers

import android.os.AsyncTask

class AllWordsScraperTask(private val postExecute: () -> Unit) :
    AsyncTask<AllWordsScraper, Void, Unit>() {

    override fun doInBackground(vararg params: AllWordsScraper) {
        val allWordsScraper = params[0]
        allWordsScraper.loadUrls()
    }

    override fun onPostExecute(e: Unit) {
        postExecute()
    }
}
