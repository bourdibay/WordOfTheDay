package com.bourdi_bay.wordoftheday

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bourdi_bay.wordoftheday.scrapers.AllWordsScraper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AllWordsScraperTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    @Test
    fun retrieveAllWordUrls() {

        val stream = context.resources.assets.open("all_words.html")

        val scraper = AllWordsScraper()
        scraper.loadFromStream(stream)

        Assert.assertEquals(778, scraper.intermediateWordsUrls.size)
        Assert.assertEquals(520, scraper.basicWordsUrls.size)
    }

}
