package com.bourdi_bay.wordoftheday

import android.os.AsyncTask
import com.bourdi_bay.wordoftheday.scrapers.DailyWordScraperTask
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class UrlsLoaderTask(
    private val urls: List<String>,
    private val progressLayout: ProgressLayout,
    private val postExecute: (List<DailyWord>) -> Unit
) : AsyncTask<Unit, Void, Unit>() {

    private val dailyWords = Collections.synchronizedList(ArrayList<DailyWord>())
    private var remainingUrls = AtomicInteger(0)

    private fun isLoaded(): Boolean {
        return remainingUrls.get() <= 0
    }

    override fun onPreExecute() {
        remainingUrls.set(urls.size)
        progressLayout.initializeProgressBar(urls.size)
    }

    override fun doInBackground(vararg params: Unit) {
        for (url in urls) {

            DailyWordScraperTask {
                remainingUrls.decrementAndGet()
                progressLayout.updateProgressBar(isLoaded())
                if (it != null) {
                    dailyWords.add(it)
                }

                if (isLoaded()) {
                    postExecute(dailyWords.toList())
                }
            }.execute(url)
        }
    }
}