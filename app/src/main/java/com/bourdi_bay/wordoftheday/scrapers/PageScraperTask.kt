package com.bourdi_bay.wordoftheday.scrapers

import android.os.AsyncTask

class PageScraperTask(
    private val level: String,
    private val postExecute: (List<PageScraper.Url>) -> Unit
) :
    AsyncTask<Integer, Void, List<PageScraper.Url>>() {

    override fun doInBackground(vararg params: Integer): List<PageScraper.Url> {
        val pageNumber = params[0]
        val scraper = PageScraper(level)
        return scraper.loadFromUrl(pageNumber.toInt())
    }

    override fun onPostExecute(urls: List<PageScraper.Url>) {
        postExecute(urls)
    }
}
