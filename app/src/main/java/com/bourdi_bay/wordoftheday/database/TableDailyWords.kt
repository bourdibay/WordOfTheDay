package com.bourdi_bay.wordoftheday.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bourdi_bay.wordoftheday.DailyWord

class TableDailyWords(val dbHandler: SQLiteOpenHelper) {
    private val TABLE = "daily_words"
    private val COL_WORD = "C_WORD"
    private val COL_DATE = "C_DATE"
    private val COL_DEFINITION = "C_DEFINITION"
    internal val COL_EXAMPLES_ID = "C_EXAMPLESID"
    internal val COL_OFTENUSED_ID = "C_OFTENUSEDID"
    private val COL_LEVEL = "C_LEVEL"

    private val CREATE_BDD = ("CREATE TABLE " + TABLE + " ("
            + COL_WORD + " TEXT NOT NULL, "
            + COL_LEVEL + " TEXT NOT NULL, "
            + COL_DATE + " TEXT NOT NULL, "
            + COL_DEFINITION + " TEXT NOT NULL, "
            + COL_EXAMPLES_ID + " INTEGER, "
            + COL_OFTENUSED_ID + " INTEGER"
            + ");")

    fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_BDD)
    }

    fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE $TABLE;")
        onCreate(db)
    }

    fun generateID(column: String): Int {
        val db: SQLiteDatabase = dbHandler.readableDatabase
        val cursor: Cursor = db.query(
            TABLE,
            arrayOf("MAX($column)"),
            null,
            null,
            null,
            null,
            null
        )

        var maxId = 0
        cursor.use {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                maxId = cursor.getInt(0) + 1 // TODO: probably not the best idea, if int overflow ?
            }
        }
        return maxId
    }

    fun save(dailyWord: DailyWord, level: String, examplesId: Int, oftenUsedId: Int) {

        val values = ContentValues().apply {
            put(COL_WORD, dailyWord.word)
            put(COL_DATE, dailyWord.date)
            put(COL_DEFINITION, dailyWord.definition)
            put(COL_EXAMPLES_ID, examplesId)
            put(COL_OFTENUSED_ID, oftenUsedId)
            put(COL_LEVEL, level)
        }

        dbHandler.writableDatabase.insert(TABLE, null, values)
    }

    fun loadMinimal(level: String): List<DailyWord> {
        val db: SQLiteDatabase = dbHandler.readableDatabase
        val cursor: Cursor = db.query(
            TABLE,
            arrayOf(COL_WORD, COL_DATE),
            "$COL_LEVEL = ?",
            arrayOf(level),
            null,
            null,
            null
        )

        val dailyWords = arrayListOf<DailyWord>()

        if (!cursor.moveToFirst()) {
            return dailyWords
        }

        cursor.use {
            while (!it.isAfterLast) {
                val word = cursor.getString(cursor.getColumnIndex(COL_WORD))
                val date = cursor.getString(cursor.getColumnIndex(COL_DATE))
                dailyWords.add(
                    DailyWord(
                        word,
                        date,
                        ""
                    )
                )
                it.moveToNext()
            }
        }
        return dailyWords.toList()
    }

    data class LoadedDailyWord(
        val dailyWord: DailyWord,
        val examplesId: Int,
        val oftenUsedId: Int
    )

    fun load(word: String): LoadedDailyWord? {
        val db: SQLiteDatabase = dbHandler.readableDatabase
        val cursor: Cursor = db.query(
            TABLE,
            arrayOf(COL_WORD, COL_DATE, COL_DEFINITION, COL_EXAMPLES_ID, COL_OFTENUSED_ID),
            "$COL_WORD = ?",
            arrayOf(word),
            null,
            null,
            null
        )

        if (cursor.count <= 0) {
            return null
        }

        cursor.moveToFirst()

        cursor.use {
            val word = cursor.getString(cursor.getColumnIndex(COL_WORD))
            val date = cursor.getString(cursor.getColumnIndex(COL_DATE))
            val examplesId = cursor.getInt(cursor.getColumnIndex(COL_EXAMPLES_ID))
            val oftenUsedId = cursor.getInt(cursor.getColumnIndex(COL_OFTENUSED_ID))
            val definition = cursor.getString(cursor.getColumnIndex(COL_DEFINITION))
            return LoadedDailyWord(
                DailyWord(
                    word,
                    date,
                    definition
                ), examplesId, oftenUsedId
            )
        }
    }
}