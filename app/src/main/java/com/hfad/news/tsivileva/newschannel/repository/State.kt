package com.hfad.news.tsivileva.newschannel.repository

import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

sealed class DownloadingState

open class DownloadingSuccessful():DownloadingState()
class DownloadedFeed(val feed:NewsItem): DownloadingSuccessful()
class DownloadedFeeds(val feedList:List<NewsItem>): DownloadingSuccessful()


class DownloadingError(val e: Throwable):DownloadingState()
class DownloadingProgress(val message: String):DownloadingState()