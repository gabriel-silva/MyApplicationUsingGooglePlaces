package com.myapplicationusinggoogleplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.widget.LoginButton
import java.util.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback

class MainActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    private var loginButton: LoginButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)

        this.callbackManager = CallbackManager.Factory.create()
        this.loginButton = findViewById(R.id.login_button) as LoginButton
        this.loginButton?.setReadPermissions(Arrays.asList("public_profile"))

        // Callback registration
        this.loginButton?.registerCallback(this.callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                Log.d("onSucess", "" + result.toString())
            }

            override fun onCancel() {
                Log.d("onCancel", "onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("onSucess", "" + error.toString())
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        this.callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
