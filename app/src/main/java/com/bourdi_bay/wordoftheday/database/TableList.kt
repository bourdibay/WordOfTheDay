package com.bourdi_bay.wordoftheday.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class TableList(
    val dbHandler: SQLiteOpenHelper,
    val TABLE: String
) {
    private val COL_ID = "C_ID"
    private val COL_ORDER = "C_ORDER"
    private val COL_SENTENCE = "C_SENTENCE"

    private val CREATE_BDD = ("CREATE TABLE " + TABLE + " ("
            + COL_ID + " INTEGER, "
            + COL_ORDER + " INTEGER, "
            + COL_SENTENCE + " TEXT"
            + ");")

    fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_BDD)
    }

    fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE $TABLE;")
        onCreate(db)
    }

    fun save(examples: List<String>, id: Int) {

        for ((order, line) in examples.withIndex()) {

            val values = ContentValues().apply {
                put(COL_ID, id)
                put(COL_ORDER, order)
                put(COL_SENTENCE, line)
            }
            dbHandler.writableDatabase.insert(TABLE, null, values)
        }
    }

    fun load(id: Int): List<String> {
        val db: SQLiteDatabase = dbHandler.readableDatabase
        val cursor: Cursor = db.query(
            TABLE,
            null,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            COL_ORDER
        )

        val lines = arrayListOf<String>()
        if (!cursor.moveToFirst()) {
            return lines
        }

        cursor.use {
            while (!it.isAfterLast) {
                val sentence = cursor.getString(cursor.getColumnIndex(COL_SENTENCE))
                lines.add(sentence)
                it.moveToNext()
            }
        }
        return lines.toList()
    }
}