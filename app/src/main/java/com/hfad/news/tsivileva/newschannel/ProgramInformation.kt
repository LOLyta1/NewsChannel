package com.hfad.news.tsivileva.newschannel

sealed class DownloadingState<T>
class DownloadingSuccessful<T>(val data: T) : DownloadingState<T>()
class DownloadingError<T>(val e: Throwable, val cachedData: T) : DownloadingState<T>()

enum class SortType {
    ASC,
    DESC
}

enum class FeedsSource(val link: String) {
    HABR("https://habr.com/ru/rss/all/"),
    PROGER("https://tproger.ru/feed/"),
    BOTH("")
}
