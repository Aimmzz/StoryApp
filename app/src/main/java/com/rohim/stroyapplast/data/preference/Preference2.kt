package com.rohim.stroyapplast.data.preference

import android.content.Context

class Preference2(context: Context) {
    private val preference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    fun setToken(token:String){
        val edit = preference.edit()
        edit.putString(TOKEN,token)
        edit.apply()
    }

    fun getToken(): String? {
        return preference.getString(TOKEN, null)
    }
    companion object{
        const val PREF_NAME = "login_pref"
        const val TOKEN = "token"
    }
}