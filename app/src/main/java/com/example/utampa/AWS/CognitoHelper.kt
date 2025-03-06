package com.example.utampa.AWS


import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions

object CognitoHelper {
    private const val CLIENT_ID = "287a4e53vrj0anmdl03bupqa5n"
    private const val POOL_ID = "us-east-2_WDDhh4cw6"
    private val REGION = Regions.US_EAST_2

    private var userPool: CognitoUserPool? = null

    fun initCognito(context: Context) {
        userPool = CognitoUserPool(context, POOL_ID, CLIENT_ID, null, REGION)
    }

    fun getUserPool(): CognitoUserPool? {
        return userPool
    }
}
