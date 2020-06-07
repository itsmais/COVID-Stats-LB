package com.example.covid19_stats_leb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*

import java.io.IOException

var cases = 0
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchJSON()
        Picasso.get()
            .load("https://disease.sh/assets/img/flags/lb.png")
            .resize(500, 500)
            .centerCrop()
            .into(imageView_flag)


        Thread(Runnable {
            // performing some dummy time taking operation
            var i=0;
            while(i<Int.MAX_VALUE){
                i++
            }

            // try to touch View of UI thread
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                textView_overall_cases_number.text = cases.toString()
            })
        }).start()

    }


}


fun fetchJSON (){
    val requestURL = "https://corona.lmao.ninja/v2/countries/lebanon"
    val client = OkHttpClient()
    val request = Request.Builder().url(requestURL).build()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val body = response?.body?.string()
            val gson = GsonBuilder().create()
            val data = gson.fromJson(body, Data::class.java)
            cases = data.cases

        }

        override fun onFailure(call: Call, e: IOException) {
            println("failed to process request")
        }

    })
}

class Data(val cases: Int, val todayCases: Int, val deaths: Int)
// class CountryInfo(val flag: String)