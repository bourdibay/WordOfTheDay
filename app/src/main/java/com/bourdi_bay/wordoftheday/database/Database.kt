package com.bourdi_bay.wordoftheday.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bourdi_bay.wordoftheday.DailyWord

class Database(context: Context) :
    SQLiteOpenHelper(context, "DailyWords.db", null, 1) {
    private val dailyWordsTable =
        TableDailyWords(this)
    private val examplesTable = TableExamples(this)
    private val oftenUsedTable = TableOftenUsed(this)

    override fun onCreate(db: SQLiteDatabase?) {
        dailyWordsTable.onCreate(db)
        examplesTable.onCreate(db)
        oftenUsedTable.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        dailyWordsTable.onUpgrade(db, p1, p2)
        examplesTable.onUpgrade(db, p1, p2)
        oftenUsedTable.onUpgrade(db, p1, p2)
    }

    @Synchronized
    fun save(dailyWord: DailyWord, level: String) {
        val examplesId = dailyWordsTable.generateID(dailyWordsTable.COL_EXAMPLES_ID)
        val oftenUsedId = dailyWordsTable.generateID(dailyWordsTable.COL_OFTENUSED_ID)

        dailyWordsTable.save(dailyWord, level, examplesId, oftenUsedId)
        examplesTable.save(dailyWord.examples, examplesId)
        oftenUsedTable.save(dailyWord.oftenUsed, oftenUsedId)
    }

    fun loadMinimal(level: String): List<DailyWord> {
        return dailyWordsTable.loadMinimal(level)
    }

    fun load(word: String): DailyWord? {
        val loadedDailyWord = dailyWordsTable.load(word) ?: return null

        val examples = examplesTable.load(loadedDailyWord.examplesId)
        val oftenUsed = oftenUsedTable.load(loadedDailyWord.oftenUsedId)

        return DailyWord(
            loadedDailyWord.dailyWord.word,
            loadedDailyWord.dailyWord.date,
            loadedDailyWord.dailyWord.definition,
            examples,
            oftenUsed
        )
    }

}