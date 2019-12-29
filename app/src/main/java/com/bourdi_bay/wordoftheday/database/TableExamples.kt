package com.bourdi_bay.wordoftheday.database

import android.database.sqlite.SQLiteOpenHelper

class TableExamples(dbHandler: SQLiteOpenHelper) : TableList(dbHandler, "examples")
