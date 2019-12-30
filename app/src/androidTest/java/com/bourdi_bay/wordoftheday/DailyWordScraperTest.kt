package com.bourdi_bay.wordoftheday

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bourdi_bay.wordoftheday.scrapers.DailyWordScraper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DailyWordScraperTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    @Test
    fun dailyWordIsBuilt() {

        val stream = context.resources.assets.open("daily_word.html")

        val scraper = DailyWordScraper()
        val dailyWord = scraper.loadFromStream(stream)

        Assert.assertNotNull(dailyWord)
        Assert.assertEquals("merry", dailyWord!!.word)
        Assert.assertEquals("December 24, 2019", dailyWord.date)
        Assert.assertEquals(
            "I’m sure you’ve been hearing and reading “Merry Christmas” a lot these days," +
                    " but do you know what merry actually means?" +
                    " Merry is an adjective that describes someone or something happy," +
                    " cheerful, and full of joy, and it is also used to describe festive occasions, like Christmas." +
                    " In the UK, merry is also a light-hearted way of saying that someone is slightly drunk.",
            dailyWord.definition
        )
        Assert.assertEquals(3, dailyWord.examples.size)
        Assert.assertEquals(
            listOf(
                "I love spending time with Sabrina, because she's always laughing and merry.",
                "On behalf of everyone at our company, we would like to wish you a very merry Christmas.",
                "I noticed Oliver was a bit merry last night; I hope he hasn't got a headache this morning!"
            ),
            dailyWord.examples
        )
        Assert.assertEquals(3, dailyWord.oftenUsed.size)
        Assert.assertEquals(
            listOf(
                "make merry: to have a good time, often involving food and alcoholic drink: Example: “At Christmas, most people get together with their family and friends to make merry.”",
                "the more the merrier: used to say someone is welcome to join a group, excursion, etc.: Example: “Is it OK if I come to the meeting next week?” “Sure, why not? The more the merrier!”",
                "merry-go-round: a carousel or, figuratively, a whirl of events: Example: “My wedding weekend was a merry-go-round of events because there was so much to do.”"
            ),
            dailyWord.oftenUsed
        )
    }

    @Test
    fun dailyWordIsBuiltWithOldHtmlFormat() {

        val stream = context.resources.assets.open("daily_word_old_format.html")

        val scraper = DailyWordScraper()
        val dailyWord = scraper.loadFromStream(stream)

        Assert.assertNotNull(dailyWord)
        Assert.assertEquals("rat", dailyWord!!.word)
        Assert.assertEquals("September 15, 2016", dailyWord.date)
    }
}