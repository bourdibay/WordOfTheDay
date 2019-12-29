package com.bourdi_bay.wordoftheday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.bourdi_bay.wordoftheday.scrapers.AllWordsScraper

class BasicListFragment : ListFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        initializeView(view, "basic")
        return view
    }

    override fun onAllWordsScraperDone(allWordsScraper: AllWordsScraper) {
        ProgressLayout(view)
        val wordListView = view?.findViewById<ListView>(R.id.wordList)

        if (wordListView != null) {
            val urls = ArrayList(allWordsScraper.basicWordsUrls)
            reloadDailyWordsFromUrls(
                wordListView.adapter as WordViewAdapter, urls,
                "basic"
            )
        }
    }
}
