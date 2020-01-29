package com.myapplicationusinggoogleplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.widget.LoginButton
import java.util.*
import com.facebook.login.LoginResult
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    private var loginButton: LoginButton? = null
    private var accessTokenTracker: AccessTokenTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)

        this.callbackManager = CallbackManager.Factory.create()
        this.loginButton = findViewById(R.id.login_button) as LoginButton
        this.loginButton?.setReadPermissions(Arrays.asList("email", "public_profile"))

        // Callback registration
        this.loginButton?.registerCallback(
            this.callbackManager,
            object : FacebookCallback<LoginResult> {
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

        this.accessTokenTracker = object : AccessTokenTracker(){
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
                if(currentAccessToken != null) {
                    loadUserProfile(currentAccessToken)
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        this.callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadUserProfile(newAccessToken: AccessToken) {
        var request: GraphRequest = GraphRequest.newMeRequest(newAccessToken, object : GraphRequest.GraphJSONObjectCallback {
            override fun onCompleted(jsonObject: JSONObject?, response: GraphResponse?) {

                try {
                    var firstName: String? = jsonObject?.getString("first_name")
                    var lastName: String? = jsonObject?.getString("last_name")
                    var id: String? = jsonObject?.getString("id")
                    val imageUrl = "https://graph.facebook.com/$id/picture?type=normal"

                    Log.e("onCompleted: ", "firstName: $firstName lastName: $lastName id: $id")

                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }

        })

        var parameters: Bundle = Bundle()
        parameters.putString("fields", "first_name,last_name,id")
        request.setParameters(parameters)
        request.executeAsync()

    }
}
