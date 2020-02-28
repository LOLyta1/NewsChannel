package com.hfad.news.tsivileva.newschannel.repository

import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

sealed class DownloadingState
class DownloadingSuccessful(val newsList:List<NewsItem>):DownloadingState()
class DownloadingError(val e: Throwable):DownloadingState()
class DownloadingProgress(val message: String):DownloadingState()