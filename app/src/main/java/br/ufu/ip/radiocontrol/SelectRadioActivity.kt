package br.ufu.ip.radiocontrol

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView

class SelectRadioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var listView = ListView(this)
        setContentView(listView)


    }

    companion object {
        const val EXTRA_STATION = "station"
        const val EXTRA_RESULT = "result"
    }

}