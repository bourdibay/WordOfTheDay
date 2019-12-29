package com.bourdi_bay.wordoftheday.database

import android.database.sqlite.SQLiteOpenHelper

class TableOftenUsed(dbHandler: SQLiteOpenHelper) : TableList(dbHandler, "often_used")
