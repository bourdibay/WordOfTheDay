package com.bourdi_bay.wordoftheday.scrapers

import com.bourdi_bay.wordoftheday.DailyWord
import org.jsoup.Jsoup
import java.io.IOException
import java.io.InputStream

class DailyWordScraper {

    private var htmlDocument: org.jsoup.nodes.Document? = null

    fun loadFromStream(stream: InputStream): DailyWord? {
        htmlDocument = null
        try {
            htmlDocument = Jsoup.parse(String(stream.readBytes()))
        } catch (e: IOException) {
        }
        return loadDailyWord()
    }

    fun loadFromUrl(url: String): DailyWord? {
        htmlDocument = null

        try {
            val response = Jsoup.connect(url).timeout(10000).ignoreHttpErrors(true).execute()

            val statusCode = response.statusCode()
            if (statusCode == 200) {
                htmlDocument = Jsoup.parse(response.body())
            }
        } catch (e: IOException) {
        }

        return loadDailyWord()
    }

    private fun loadDailyWord(): DailyWord? {
        if (htmlDocument == null)
            return null

        val divSectionWords = htmlDocument!!.select("div.section.word p")
        val divDates = htmlDocument!!.select("div.post-date p")
        if (divSectionWords.isEmpty() || divDates.isEmpty())
            return null

        val word = divSectionWords[0].ownText()
        val date = divDates[0].text()

        val divPostEntry = htmlDocument!!.select("div.post-entry")
        if (divPostEntry.isEmpty())
            return null

        val divDefinition = divPostEntry.select("div.section.text-area")
        if (divDefinition.isEmpty())
            return null
        val definition = divDefinition[0].text()

        val divExamples = divPostEntry.select("div.section.list-w-title ul")
        if (divExamples.isEmpty())
            return null
        val linesExamples = divExamples.select("li")
        val examples = ArrayList(linesExamples.map { line -> line.text() })

        // Used for "Often used", "In pop culture", "Did you know?", "Origin"
        val divSectionText = divPostEntry.select("div.section.text-w-title")
        if (divSectionText.isEmpty())
            return null
        val divOftenUsed = divSectionText[0]
        val divOftenUsedText = divOftenUsed.select("div.text")
        if (divOftenUsedText.isEmpty())
            return null
        val linesOftenUsed = divOftenUsedText.select("p")
        val oftenUsed = ArrayList(linesOftenUsed.map { line -> line.text() })

        return DailyWord(word, date, definition, examples, oftenUsed)
    }

}