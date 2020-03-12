package com.hfad.news.tsivileva.newschannel.activity

interface IPermissionListener {
    fun getPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
}