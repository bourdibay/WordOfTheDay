package com.bourdi_bay.wordoftheday.scrapers

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.io.InputStream

class AllWordsScraper {

    private val url = "https://daily.wordreference.com/all-words/"
    private var htmlDocument: org.jsoup.nodes.Document? = null

    var intermediateWordsUrls = listOf<String>()
        private set
    var basicWordsUrls = listOf<String>()
        private set

    fun loadFromStream(stream: InputStream) {
        htmlDocument = null
        try {
            htmlDocument = Jsoup.parse(String(stream.readBytes()))
        } catch (e: IOException) {
        }

        loadLists()
    }

    fun loadUrls() {
        htmlDocument = null

        try {
            val response = Jsoup.connect(url).timeout(10000).ignoreHttpErrors(true).execute()

            val statusCode = response.statusCode()
            if (statusCode == 200) {
                htmlDocument = Jsoup.parse(response.body())
            }
        } catch (e: IOException) {
        }

        loadLists()
    }

    private fun loadLists() {

        if (htmlDocument == null)
            return

        val halfParts = htmlDocument!!.select("div.half-part")

        if (halfParts.size < 2)
            return

        intermediateWordsUrls = loadHalfPart(halfParts[0])
        basicWordsUrls = loadHalfPart(halfParts[1])
    }

    private fun loadHalfPart(div: Element): List<String> {
        val headers = div.select("h3")

        val references = headers.map { header ->
            header.select("a")
        }

        val referencesWithOnlyWords = references.filter { ref ->
            val word = ref.text()
            !word.startsWith("termediate+") && !word.startsWith("sic+")
        }

        return referencesWithOnlyWords.map { ref -> ref.attr("href") }
    }

}