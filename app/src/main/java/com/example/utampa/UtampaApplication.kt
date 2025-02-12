package com.example.utampa
import android.app.Application

class UtampaApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: UtampaApplication
            private set
    }
}
