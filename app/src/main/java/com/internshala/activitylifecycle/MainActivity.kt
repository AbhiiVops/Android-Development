package com.internshala.activitylifecycle

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import java.net.CookieHandler


class MainActivity : AppCompatActivity() {


    var titleName: String? = "Codeguru"

    lateinit var etMessage: EditText
    lateinit var btnSend: Button
    lateinit var btnLogout :Button
    lateinit var marqueeText : TextView

    lateinit var sharedPreferences: SharedPreferences

    private lateinit var webView: WebView

    var userIsAuthenticated = false
    lateinit var account: Auth0

    lateinit var btnExploreforJava:Button
    lateinit var btnExploreWebDev: Button
    lateinit var btnExploreGitHub: Button
    lateinit var btnBuyNowDsaWithJava: Button
    lateinit var btnExploreGoLang: Button

    private var currentLayoutId = R.layout.activity_main_dashboard



    override fun onCreate(savedInstanceState: Bundle?) {    // This onCreate() is mandatory and need to be present in every app
        super.onCreate(savedInstanceState)  // OnCreate is a method of the parent class


  //      Toast.makeText(this@MainActivity, "Firbase Connection Success",Toast.LENGTH_LONG).show()


        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        setContentView(R.layout.activity_main_dashboard)  // R is the res file which consists the id's of all the resources used
        // setContentView extract the layout from the layout directory and sets it on the main screen
        // in short the setContentView is responsible for setting the ui of the app

        println("onCreate called")

        // When we open a video in youtube and when we rotate the screen the videos continues to play
        // but in the backend the present activity gets destroyed andnew activity gets created

        // so how do we let a video play when the screen is rotated?
        // Override the OnSaveInstanceState() function.
        // Before this,save the instance of the activity in a bundle
        // pass this bundle in the argument of the SaveInstanceState() function
        // this is done to create seamless experience

        // What are bundles and what it is used for?
        // A bundle is used to pass data from one activity to another. This data can be in any form,
        // like strings,Integers,double etc.

        btnExploreforJava = findViewById(R.id.btnExploreforJava)
        btnExploreWebDev = findViewById(R.id.btnExploreWebDev)
        btnExploreGitHub = findViewById(R.id.btnExploreGitHub)
        btnBuyNowDsaWithJava= findViewById(R.id.btnBuyNowDsaWithJava)
        btnExploreGoLang = findViewById(R.id.btnExploreGoLang)







        btnExploreforJava.setOnClickListener {
            setContentView(R.layout.java_course)
        }

        btnExploreWebDev.setOnClickListener {
            setContentView(R.layout.web_devoplopment)
        }

        btnExploreGitHub.setOnClickListener {
            setContentView(R.layout.github)
        }

        btnBuyNowDsaWithJava.setOnClickListener {
            setContentView(R.layout.dsa_with_java)
        }

        btnExploreGoLang.setOnClickListener {
            setContentView(R.layout.go_lang)
        }






        titleName = sharedPreferences.getString("Title","The Avengers")

//        if (intent != null) {
//            titleName = intent.getStringExtra("Name")
//        }


        //  title = "The Avengers"

        title = titleName



        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        btnLogout= findViewById(R.id.btnLogout)

        marqueeText = findViewById(R.id.marqueeText)
        marqueeText.isSelected = true



        btnLogout.setOnClickListener {

           logout()

            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
           // sharedPreferences.edit().clear().apply()
          //  finish()

        }





        btnSend.setOnClickListener {

            val message: String? = etMessage.text.toString()

            if (message?.isNotEmpty() == true) {

                val intent = Intent(this@MainActivity, Message_Activity::class.java)
                intent.putExtra("Message", message)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Write a message!", Toast.LENGTH_SHORT).show()
            }


        }



    }


    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(
                this,
                object : Callback<Void?, AuthenticationException> {
                    override fun onSuccess(result: Void?) {
                        // The user has been logged out!
                        Toast.makeText(
                            this@MainActivity,
                            "Successfully logged out!",
                            Toast.LENGTH_SHORT
                        ).show()

                        CookieManager.getInstance().removeAllCookies(null)
                        CookieManager.getInstance().flush()
                        webView.clearCache(true)

                        // Send logout request using Volley
                        val queue = Volley.newRequestQueue(this@MainActivity)
                        val logoutUrl = "https://codeguru.us.auth0.com/v2/logout?federated"
                        val stringRequest = StringRequest(
                            Request.Method.GET, logoutUrl,
                            { response ->
                                // Handle the response
                                Toast.makeText(this@MainActivity, "Successfully logged out!", Toast.LENGTH_SHORT).show()
                                webView.loadUrl("app://codeguru.us.auth0.com/android/com.internshala.activitylifecycle/callback")
                            },
                            { error ->
                                // Handle the error
                                Toast.makeText(this@MainActivity, "Couldn't Logout!", Toast.LENGTH_SHORT).show()
                            }
                        )
                        queue.add(stringRequest)
                    }

                    override fun onFailure(error: AuthenticationException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Couldn't Logout!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

          fun onBackPressed() {
            // set the content view to activity_main_dashboard layout
            setContentView(R.layout.activity_main_dashboard)
        }
    }







}

