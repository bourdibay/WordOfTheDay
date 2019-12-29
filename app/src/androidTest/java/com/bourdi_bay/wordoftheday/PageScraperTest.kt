package com.bourdi_bay.wordoftheday

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bourdi_bay.wordoftheday.scrapers.PageScraper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PageScraperTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    @Test
    fun retrieveAllWordUrls() {

        val stream = context.resources.assets.open("front_page_words.html")

        val scraper = PageScraper("intermediate")
        val urls = scraper.loadFromStream(stream)

        Assert.assertEquals(10, urls.size)
        val wordNames = urls.map { it.word }
        Assert.assertEquals(
            listOf(
                "freeze",
                "mince",
                "humbug",
                "merry",
                "stock",
                "pill",
                "fringe",
                "perk",
                "mess",
                "sheer"
            ), wordNames
        )
        val urlValues = urls.map { it.url }
        Assert.assertEquals(
            listOf(
                "https://daily.wordreference.com/2019/12/27/intermediate-word-of-the-day-freeze/",
                "https://daily.wordreference.com/2019/12/26/intermediate-word-of-the-day-mince/",
                "https://daily.wordreference.com/2019/12/25/intermediate-word-of-the-day-humbug/",
                "https://daily.wordreference.com/2019/12/24/intermediate-word-of-the-day-merry/",
                "https://daily.wordreference.com/2019/12/23/intermediate-word-of-the-day-stock/",
                "https://daily.wordreference.com/2019/12/20/intermediate-word-of-the-day-pill/",
                "https://daily.wordreference.com/2019/12/19/intermediate-word-of-the-day-fringe/",
                "https://daily.wordreference.com/2019/12/18/intermediate-word-of-the-day-perk/",
                "https://daily.wordreference.com/2019/12/17/intermediate-word-of-the-day-mess/",
                "https://daily.wordreference.com/2019/12/16/intermediate-word-of-the-day-sheer/"
            ), urlValues
        )

    }

}
