package com.hfad.news.tsivileva.newschannel.repository

import com.hfad.news.tsivileva.newschannel.repository.local.News

sealed class DownloadingState

open class DownloadingSuccessful():DownloadingState()
class DownloadedFeed(val feed: News?): DownloadingSuccessful()
class DownloadedFeeds(val feedList:List<News>): DownloadingSuccessful()

class DownloadingError(val e: Throwable):DownloadingState()
class DownloadingProgress(val message: String):DownloadingState()

//class Downloaded<T:NewsItem>(val feed:T): DownloadingSuccessful()

