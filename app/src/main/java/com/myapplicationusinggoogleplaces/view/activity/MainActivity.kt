package com.myapplicationusinggoogleplaces.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.myapplicationusinggoogleplaces.R
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    private var loginButton: LoginButton? = null
    private var imageViewFacebook: ImageView? = null
    private var textViewName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        this.imageViewFacebook = findViewById(R.id.image_facebook)
        this.textViewName = findViewById(R.id.text_view_name)

        this.callbackManager = CallbackManager.Factory.create()
        this.loginButton = findViewById(R.id.login_button)
        this.loginButton?.setReadPermissions(listOf("email", "public_profile"))

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
                    Log.d("onError", "" + error.toString())
                }

            })

        var accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker(){
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
                if(currentAccessToken == null) {
                    imageViewFacebook?.setImageResource(0)
                    textViewName?.setText("")
                } else {
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

                    textViewName?.setText("$firstName $lastName")

                    Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewFacebook!!)

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
