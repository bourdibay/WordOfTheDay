package com.bourdi_bay.wordoftheday

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class WordViewAdapter(items: List<DailyWord>, ctx: Context) :
    ArrayAdapter<DailyWord>(ctx, R.layout.word, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var retView = convertView

        val dailyWord: DailyWord? = getItem(position)

        if (convertView == null) {
            retView =
                LayoutInflater.from(context).inflate(R.layout.word, parent, false)
        }

        val selectedWord = retView!!.findViewById<View>(R.id.wordOfTheDay) as TextView
        selectedWord.text = dailyWord?.word

        val date = retView.findViewById<View>(R.id.date) as TextView
        date.text = dailyWord?.date

        return retView
    }

}
