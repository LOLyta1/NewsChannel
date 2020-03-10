package com.hfad.news.tsivileva.newschannel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hfad.news.tsivileva.newschannel.model.local.*
import com.hfad.news.tsivileva.newschannel.repository.local.ILocaApi
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var database: NewsDatabase
    private lateinit var api: ILocaApi


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
                context, NewsDatabase::class.java).build()
        api = database.getApi()
        api.cleareNewsDescription()
        api.cleareNewsContent()
        api.cleareFav()

    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(IOException::class)
    fun selectAllDescriptionsTest() {
        val descriptions = mutableListOf(
                NewsDescription(id = 125165, date = Date(1581447890000), picture = "https://tproger.ru/apple-touch-icon.png", sourceKind = FeedsSource.PROGER, link = "https://tproger.ru/events/gtp-indie-cup-winter-2020/", title = "15 января – 2 апреля, онлайн: конкурс Indie Cup'"),
                NewsDescription(id = 491362, date = Date(1583504198000), picture = "https://habrastorage.org/webt/fb/i8/iy/fbi8iyxcyyzik7-ou5-txjc7s8a.jpeg", sourceKind = FeedsSource.HABR, link = "https://habr.com/ru/post/491362/", title = "Java-дайджест за 6 марта")
        )
        api.insertIntoDescription(descriptions)
        val byName = api.selectAllDescriptions()
        printNewsDescriptionList(byName)
        Assert.assertTrue(byName.isNotEmpty())
    }

    @Test
    @Throws(IOException::class)
    fun selectDescriptionByTitle() {
        val news = NewsDescription(id = 125165, date = Date(1581447890000), picture = "https://tproger.ru/apple-touch-icon.png", sourceKind = FeedsSource.PROGER, link = "https://tproger.ru/events/gtp-indie-cup-winter-2020/", title = "15 января – 2 апреля, онлайн: конкурс Indie Cup")
        api.insertIntoDescription(news)
        val result = api.selectDescriptionByTitle("15 января – 2 апреля, онлайн: конкурс Indie Cup")
        Assert.assertEquals(result[0].id, 125165)
    }

    @Test
    @Throws(IOException::class)
    fun insertContentTest() {
        val news = NewsDescription(id = 125165, date = Date(1581447890000), picture = "https://tproger.ru/apple-touch-icon.png", sourceKind = FeedsSource.PROGER, link = "https://tproger.ru/events/gtp-indie-cup-winter-2020/", title = "15 января – 2 апреля, онлайн: конкурс Indie Cup")
        api.insertIntoDescription(news)
        val id = api.insertContent(NewsContent(null, 125165, "awodioawjd"))
        Assert.assertEquals(id, 1)
    }

    @Test
    @Throws(IOException::class)
    fun selectDescriptionAndContentTest() {
        val description = NewsDescription(id = 125165, date = Date(1581447890000), picture = "https://tproger.ru/apple-touch-icon.png", sourceKind = FeedsSource.PROGER, link = "https://tproger.ru/events/gtp-indie-cup-winter-2020/", title = "15 января – 2 апреля, онлайн: конкурс Indie Cup")
        val content = NewsContent(null, 125165, "awodioawjd")
        api.insertIntoDescription(description)
        val idContent = api.insertContent(content)
        content.id = idContent
        Assert.assertEquals(api.selectDescriptionAndContent(125165), NewsAndContent(description, content))
    }

    @Test
    @Throws(IOException::class)
    fun selectDescriptionAndFawTest() {
        var description = NewsDescription(id = 125165, date = Date(1581447890000), picture = "https://tproger.ru/apple-touch-icon.png", sourceKind = FeedsSource.PROGER, link = "https://tproger.ru/events/gtp-indie-cup-winter-2020/", title = "15 января – 2 апреля, онлайн: конкурс Indie Cup")
        var fav = Favorite(null, 125165, true)
        api.insertIntoDescription(description)
        api.insertIntoFav(fav)

        var newsAndFav = api.selectDescriptionAndFaw()
        printNewsAndFav(newsAndFav)

        description = NewsDescription(id = 491362, date = Date(1583504198000), picture = "https://habrastorage.org/webt/fb/i8/iy/fbi8iyxcyyzik7-ou5-txjc7s8a.jpeg", sourceKind = FeedsSource.HABR, link = "https://habr.com/ru/post/491362/", title = "Java-дайджест за 6 марта")
        api.insertIntoDescription(description)

       newsAndFav = api.selectDescriptionAndFaw()
        printNewsAndFav(newsAndFav)

        Assert.assertEquals(newsAndFav.count(), 1)
    }

    @Test
    @Throws(IOException::class)
    fun selectAllDescriptionAndFavTest() {
        var description = NewsDescription(id = 125165, date = Date(1581447890000), picture = "https://tproger.ru/apple-touch-icon.png", sourceKind = FeedsSource.PROGER, link = "https://tproger.ru/events/gtp-indie-cup-winter-2020/", title = "15 января – 2 апреля, онлайн: конкурс Indie Cup")
        var fav = Favorite(null, 125165, true)

        api.insertIntoDescription(description)
        api.insertIntoFav(fav)

        description = NewsDescription(id = 491362, date = Date(1583504198000), picture = "https://habrastorage.org/webt/fb/i8/iy/fbi8iyxcyyzik7-ou5-txjc7s8a.jpeg", sourceKind = FeedsSource.HABR, link = "https://habr.com/ru/post/491362/", title = "Java-дайджест за 6 марта")
        api.insertIntoDescription(description)

        description = NewsDescription(id = 491000, date = Date(1583504198000), picture = "https://habrastorage.org/webt/fb/i8/iy/fbi8iyxcyyzik7-ou5-txjc7s8a.jpeg", sourceKind = FeedsSource.HABR, link = "https://habr.com/ru/post/491362/", title = "Java-дайджест за 6 марта")
        api.insertIntoDescription(description)

        val newsAndFav = api.selectAllDescriptionAndFav()
        printNewsAndFav(newsAndFav)
        Assert.assertEquals(newsAndFav.count(), 3)
    }


}