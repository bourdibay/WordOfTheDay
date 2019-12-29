package com.bourdi_bay.wordoftheday.scrapers

import android.widget.Toast
import org.jsoup.Jsoup
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern

class PageScraper(private val level: String) {
    private var htmlDocument: org.jsoup.nodes.Document? = null

    data class Url(val url: String, val word: String)

    fun loadFromStream(stream: InputStream): List<Url> {
        htmlDocument = null
        try {
            htmlDocument = Jsoup.parse(String(stream.readBytes()))
        } catch (e: IOException) {
        }
        return getUrls()
    }

    fun loadFromUrl(page: Int): List<Url> {
        htmlDocument = null

        try {
            // FIXME: I don't like integrating the level to the url, a separate class would be better
            val url = "https://daily.wordreference.com/$level-word-of-the-day/page/$page/"

            val response =
                Jsoup.connect(url).timeout(10000).ignoreHttpErrors(true).followRedirects(true)
                    .execute()

            val statusCode = response.statusCode()
            if (statusCode == 200) {
                htmlDocument = Jsoup.parse(response.body())
            }
        } catch (e: IOException) {
        }

        return getUrls()
    }

    private fun getUrls(): List<Url> {
        val urls = arrayListOf<Url>()

        if (htmlDocument == null)
            return urls

        val articles = htmlDocument!!.select("article")
        for (article in articles) {
            val wordElement = article.select("div.post-header a")

            if (wordElement.isEmpty())
                continue

            val url = wordElement.attr("href")

            val text = wordElement.text()
            val pattern = Pattern.compile("Word of the Day: (.*)")
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                val word = matcher.group(1)

                urls.add(Url(url, word))
            }
        }
        return urls.toList()
    }
}