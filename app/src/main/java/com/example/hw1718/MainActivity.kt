package com.example.hw1718

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

class MainActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    var id = ""
    var name = ""
    var email = ""
    var accessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val crashButton = findViewById<Button>(R.id.buttonCrash)
        val resultTextView = findViewById<TextView>(R.id.textView)
        val facebookLogin = findViewById<Button>(R.id.facebookLogin)

        crashButton.setOnClickListener {
            resultTextView.text = (5 / 0).toString()
        }
        callbackManager = CallbackManager.Factory.create()

        facebookLogin.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
        }

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {

                override fun onSuccess(loginResult: LoginResult?) {
                    getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields", "id, name, picture, email"
        )

        GraphRequest(
            token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->

                val jsonObject = response.jsonObject

                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }
                accessToken = token.toString()

                id = if (jsonObject.has("id")) {
                    val facebookId = jsonObject.getString("id")
                    facebookId.toString()
                } else {
                    "Not exists"
                }
                name = if (jsonObject.has("name")) {
                    val facebookName = jsonObject.getString("name")
                    facebookName
                } else {
                    "Not exists"
                }
                email = if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    facebookEmail
                } else {
                    "Not exists"
                }
                openDetailsActivity()
            }).executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDetailsActivity() {
        val myIntent = Intent(this, DetailsActivity::class.java)
        myIntent.putExtra("facebook_id", id)
        myIntent.putExtra("facebook_name", name)
        myIntent.putExtra("facebook_email", email)
        myIntent.putExtra("facebook_access_token", accessToken)
        startActivity(myIntent)
    }
}




