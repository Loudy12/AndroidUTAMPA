package com.example.utampa
import android.app.Application
import com.example.utampa.AWS.AWSIAMCredentialsManager

class UtampaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        AWSIAMCredentialsManager.appContext = applicationContext
    }

    companion object {
        lateinit var instance: UtampaApplication
            private set
    }
}
