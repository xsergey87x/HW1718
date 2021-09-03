package com.example.hw1718

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val facebookId = intent.getStringExtra("facebook_id")
        val facebookName = intent.getStringExtra("facebook_name")
        val facebookEmail = intent.getStringExtra("facebook_email")
        val facebookAccessToken = intent.getStringExtra("facebook_access_token")

        findViewById<TextView>(R.id.facebook_id_textview).text = facebookId
        findViewById<TextView>(R.id.facebook_name_textview).text = facebookName
        findViewById<TextView>(R.id.facebook_email_textview).text = facebookEmail
        findViewById<TextView>(R.id.facebook_access_token_textview).text = facebookAccessToken

    }
}
