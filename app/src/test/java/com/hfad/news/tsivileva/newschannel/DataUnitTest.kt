package com.hfad.news.tsivileva.newschannel

import org.junit.Test
import java.text.SimpleDateFormat

import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class DataUnitTest {
   val TO_DATE_PATTERN="dd MMM yyyy, hh:mm"

    @Test
    fun test_simple_proger(){
        val dateProger="Mon, 24 Feb 2020 15:58:09 +0000"
        val from = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.US)
        val to=SimpleDateFormat(TO_DATE_PATTERN)
        val date:Date?=from.parse(dateProger)
       print(to.format(date?.time))
    }

    @Test
    fun test_simple_habr(){
        val dateHabr="Mon, 24 Feb 2020 11:05:01 GMT"
        val from=SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.US)
        val to=SimpleDateFormat(TO_DATE_PATTERN)
        val date:Date?=from.parse(dateHabr)
        print(to.format(date?.time))


    }

    @Test
    fun test_simple_habr_item(){
        var dateString="2020-02-24T14:00Z"
        val from=SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'",Locale.US)
        val to=SimpleDateFormat(TO_DATE_PATTERN)
        val date:Date?=from.parse(dateString)
        print(to.format(date?.time))
    //    print("${localDate.dayOfMonth} ${localDate.month} ${localDate.year}, ${localDate.hour}:${localDate.minute}:${localDate.second}")


    }

    @Test
    fun test_simple_proger_item(){
        var dateString="2020-01-28T11:13:25+00:00"
        val startGmtIndex=dateString.indexOf("+")

        var gmt=dateString.substring(startGmtIndex,dateString.length)
        //gmt=gmt.replace("+"," ")
        gmt=gmt.replace(":","")


        dateString=dateString.substring(0, startGmtIndex)+gmt


        val from=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",Locale.US)

        val to=SimpleDateFormat(TO_DATE_PATTERN)
        val date:Date?=from.parse(dateString)
        print(to.format(date?.time))

        /*  val dateAccessor : TemporalAccessor? =DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date)
          val localDate=LocalDateTime.from(dateAccessor)
          print("${localDate.dayOfMonth} ${localDate.month} ${localDate.year}, ${localDate.hour}:${localDate.minute}:${localDate.second}")
      */}


}