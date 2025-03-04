package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.content.Context

/**
 * Created by viral on 26-07-2024.
 */
class App : Application() {

    companion object {
        var sApplication: App? = null

        fun getApplication(): App? {
            return sApplication
        }

        fun getContext(): Context? {
            return getApplication()?.applicationContext
        }
    }

    private var mCurrentActivity: Activity? = null

    fun getCurrentActivity(): Activity? {
        return mCurrentActivity
    }

    fun setCurrentActivity(mCurrentActivity: Activity) {
        this.mCurrentActivity = mCurrentActivity
    }


    override fun onCreate() {
        super.onCreate()
        sApplication = this
    }

}