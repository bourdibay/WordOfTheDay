package com.bourdi_bay.wordoftheday.database

import android.os.AsyncTask
import com.bourdi_bay.wordoftheday.DailyWord

class DatabaseSaveTask(
    private val dbHelper: Database,
    private val dailyWords: List<DailyWord>,
    private val level: String
) :
    AsyncTask<Unit, Void, Unit>() {
    override fun doInBackground(vararg param: Unit) {
        for (dailyWord in dailyWords) {
            dbHelper.save(dailyWord, level)
        }
    }
}