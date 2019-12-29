package com.bourdi_bay.wordoftheday.database

import android.os.AsyncTask
import com.bourdi_bay.wordoftheday.DailyWord

class DatabaseLoadMinimalTask(
    private val dbHelper: Database,
    private val level: String,
    private val postExecute: (List<DailyWord>) -> Unit
) :
    AsyncTask<Unit, Void, List<DailyWord>>() {

    override fun doInBackground(vararg param: Unit): List<DailyWord> {
        return dbHelper.loadMinimal(level)
    }

    override fun onPostExecute(dailyWords: List<DailyWord>) {
        postExecute(dailyWords)
    }
}