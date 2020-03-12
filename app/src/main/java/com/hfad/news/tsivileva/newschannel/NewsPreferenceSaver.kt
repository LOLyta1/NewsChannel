package com.hfad.news.tsivileva.newschannel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class NewsPreferenceSaver {
    fun getPreference(context:Context?) : Filters {
        var pref=context?.getSharedPreferences("com.hfad.news.tsivileva.newschannel.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
                ?.getString("preference","")
        val prefObj= Gson().fromJson(pref,Filters::class.java)
        // Log.d(DEBUG_LOG,"FragmentFeeds.getPreference(), обьект настроек - ссылка ${prefObj.source.link}, показывать только избранное ${prefObj.showOnlyFav}, сортирова - ${prefObj.sortType} ")
        return if(prefObj!=null){
            Log.d(DEBUG_LOG,"FragmentFeeds.getPreference(), обьект настроек не пустой ")
            prefObj
        }else{
            Log.d(DEBUG_LOG,"FragmentFeeds.getPreference(), обьект настроек пустой. сделали по умолчанию")
            Filters(sort = SortType.ASC,showOnlyFav = false,source = FeedsSource.BOTH)
        }
    }

    fun setPreference(pref:Filters, context: Context?){
        var preferenceEditor: SharedPreferences.Editor?=context?.getSharedPreferences("com.hfad.news.tsivileva.newschannel.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)?.edit()
        var pref= Gson().toJson(pref)
        Log.d(DEBUG_LOG,"FragmentFeeds.setPreference() - строка настроект $pref")

        preferenceEditor?.putString("preference",pref)
        preferenceEditor?.apply()
    }

}