package com.hfad.news.tsivileva.newschannel.repository

import com.hfad.news.tsivileva.newschannel.repository.local.NewsDescription

sealed class DownloadingState<T>
class  DownloadingSuccessful<T>(val data:T):DownloadingState<T>()
class DownloadingError<T>(val e: Throwable,val cachedData: T):DownloadingState<T>()



