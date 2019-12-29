package com.bourdi_bay.wordoftheday

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.bourdi_bay.wordoftheday.database.Database
import com.bourdi_bay.wordoftheday.database.DatabaseLoadMinimalTask
import com.bourdi_bay.wordoftheday.database.DatabaseLoadWordTask
import com.bourdi_bay.wordoftheday.database.DatabaseSaveTask
import com.bourdi_bay.wordoftheday.scrapers.AllWordsScraper
import com.bourdi_bay.wordoftheday.scrapers.AllWordsScraperTask
import com.bourdi_bay.wordoftheday.scrapers.PageScraperTask


open class ListFragment : Fragment() {

    private lateinit var dbHelper: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    private fun displayWord(dailyWord: DailyWord) {
        val intent = Intent(activity, DailyWordActivity::class.java)
        val b = Bundle()
        b.putParcelable("dailyWord", dailyWord)
        intent.putExtras(b)

        startActivity(intent)
    }

    private fun loadAndDisplayWord(dailyWord: DailyWord) {
        DatabaseLoadWordTask(dbHelper, dailyWord.word) {
            if (it != null) {
                dailyWord.definition = it.definition
                dailyWord.examples = it.examples
                dailyWord.oftenUsed = it.oftenUsed
            }

            displayWord(dailyWord)
        }.execute()
    }

    // level = "basic" or "intermediate"
    protected fun initializeView(view: View, level: String) {
        val wordListView = view.findViewById<ListView>(R.id.wordList)
        wordListView.adapter = WordViewAdapter(arrayListOf(), activity!!.applicationContext)

        wordListView.setOnItemClickListener { _, _, position, _ ->
            val dailyWord = wordListView.getItemAtPosition(position) as DailyWord

            if (dailyWord.definition.isEmpty()) {
                loadAndDisplayWord(dailyWord)
            } else {
                displayWord(dailyWord)
            }
        }

        loadAllWordsFromDatabase(level, wordListView.adapter as WordViewAdapter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHelper = Database(context)
    }

    override fun onDetach() {
        super.onDetach()
        dbHelper.close()
    }

    private fun loadLastMissingUrls(
        level: String,
        currentWords: HashSet<String>,
        postExecute: (List<String>) -> Unit,
        pageNumber: Integer
    ) {
        PageScraperTask(level) { loadedWords ->
            val newWords = loadedWords.filter { pageUrl -> !currentWords.contains(pageUrl.word) }

            if (newWords.isNotEmpty()) {
                postExecute(newWords.map { it.url })
            }

            if (newWords.size == loadedWords.size && loadedWords.isNotEmpty()) {
                val nextPage = pageNumber.toInt() + 1
                Toast.makeText(
                    context,
                    "Refresh last words (level $level, page $nextPage) ...",
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadLastMissingUrls(level, currentWords, postExecute, Integer(nextPage))
            }
        }.execute(pageNumber)
    }

    private fun loadLastMissingUrls(
        level: String,
        currentWords: HashSet<String>,
        postExecute: (List<String>) -> Unit
    ) {
        loadLastMissingUrls(level, currentWords, postExecute, Integer(1))
    }

    private fun loadAllWordsFromDatabase(
        level: String,
        adapter: WordViewAdapter
    ) {
        DatabaseLoadMinimalTask(
            dbHelper,
            level
        ) { loadedDailyWords ->

            refreshAdapter(loadedDailyWords, adapter)

            if (loadedDailyWords.isEmpty()) {
                Toast.makeText(
                    context,
                    "Load all existing words (level $level)",
                    Toast.LENGTH_SHORT
                ).show()
                reloadAllDailyWords()
            } else {
                Toast.makeText(context, "Refresh last words (level $level) ...", Toast.LENGTH_SHORT)
                    .show()

                val copyLoadedDailyWords = ArrayList<DailyWord>(loadedDailyWords)
                val wordsSet: HashSet<String> = HashSet(loadedDailyWords.map { it.word })

                loadLastMissingUrls(level, wordsSet) { urls ->

                    val progressLayout = ProgressLayout(view)

                    UrlsLoaderTask(
                        urls,
                        progressLayout
                    ) {
                        saveToDatabase(it, level)
                        copyLoadedDailyWords.addAll(it)
                        refreshAdapter(copyLoadedDailyWords, adapter)
                    }.execute()
                }
            }
        }.execute()
    }

    private fun refreshAdapter(dailyWords: List<DailyWord>, wordViewAdapter: WordViewAdapter) {
        wordViewAdapter.clear()
        val sortedWords = dailyWords.sortedWith(compareByDescending { it.encodedDate })
        for (dailyWord in sortedWords) {
            wordViewAdapter.add(dailyWord)
        }
        wordViewAdapter.notifyDataSetChanged()
    }

    private fun saveToDatabase(dailyWords: List<DailyWord>, level: String) {
        DatabaseSaveTask(dbHelper, dailyWords, level).execute()
    }

    protected fun reloadDailyWordsFromUrls(
        adapter: WordViewAdapter,
        urls: List<String>,
        level: String
    ) {
        val progressLayout = ProgressLayout(view)

        UrlsLoaderTask(
            urls,
            progressLayout
        ) {
            saveToDatabase(it, level)
            refreshAdapter(it, adapter)
        }.execute()
    }

    open fun onAllWordsScraperDone(allWordsScraper: AllWordsScraper) {
    }

    private fun reloadAllDailyWords() {
        val allWordsScraper = AllWordsScraper()

        AllWordsScraperTask {
            onAllWordsScraperDone(allWordsScraper)
        }.execute(allWordsScraper)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val searchAction = menu.findItem(R.id.action_search)
        val searchView = searchAction.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val wordListView = view?.findViewById<ListView>(R.id.wordList)
                (wordListView?.adapter as WordViewAdapter).filter.filter(query)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
