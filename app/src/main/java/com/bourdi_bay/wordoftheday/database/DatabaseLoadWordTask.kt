package com.bourdi_bay.wordoftheday.database

import android.os.AsyncTask
import com.bourdi_bay.wordoftheday.DailyWord

class DatabaseLoadWordTask(
    private val dbHelper: Database,
    private val word: String,
    private val postExecute: (DailyWord?) -> Unit
) :
    AsyncTask<Unit, Void, DailyWord?>() {

    override fun doInBackground(vararg param: Unit): DailyWord? {
        return dbHelper.load(word)
    }

    override fun onPostExecute(dailyWord: DailyWord?) {
        postExecute(dailyWord)
    }
}