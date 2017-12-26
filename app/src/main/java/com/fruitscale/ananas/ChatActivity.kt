package com.fruitscale.ananas

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val circleProfilePicture = CircleImageView(baseContext)
        circleProfilePicture.setImageResource(R.drawable.ic_person_black_48dp)
        circleProfilePicture.setFillColorResource(R.color.colorAccent)
        supportActionBar?.setLogo(circleProfilePicture.drawable)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        supportActionBar?.title = "Mom"
        supportActionBar?.subtitle = "Last seen at 19:17"
    }
}
