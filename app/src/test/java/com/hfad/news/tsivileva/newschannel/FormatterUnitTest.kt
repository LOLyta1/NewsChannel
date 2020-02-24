package com.hfad.news.tsivileva.newschannel

import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FormatterUnitTest {
   val TO_DATE_PATTERN="dd MMM yyyy, hh:mm"

    @Test
    fun test_simple_proger(){
        val dateProger="Mon, 24 Feb 2020 07:39:13 +0000"
        val formatter=DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z",Locale.US)
        val date=formatter.parse(dateProger)
       print(date)
    }

    @Test
    fun test_simple_habr(){
        val dateProger="Mon, 24 Feb 2020 11:05:01 GMT"
        val formatter=DateTimeFormatter.RFC_1123_DATE_TIME
        val accessor=formatter.parse(dateProger)
        val localDate=LocalDateTime.from(accessor)
        print("${localDate.dayOfMonth} ${localDate.month} ${localDate.year}, ${localDate.hour}:${localDate.minute}:${localDate.second}")


    }

    @Test
    fun test_simple_habr_item(){
        var dateString="2020-02-24T14:00Z"
        val accessor=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'", Locale.US).parse(dateString)
        val localDate=LocalDateTime.from(accessor)
        print("${localDate.dayOfMonth} ${localDate.month} ${localDate.year}, ${localDate.hour}:${localDate.minute}:${localDate.second}")


    }

    @Test
    fun test_simple_proger_item(){
        val date="2020-02-24T14:38:10+00:00"
        val dateAccessor : TemporalAccessor? =DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date)
        val localDate=LocalDateTime.from(dateAccessor)
        print("${localDate.dayOfMonth} ${localDate.month} ${localDate.year}, ${localDate.hour}:${localDate.minute}:${localDate.second}")
    }


}