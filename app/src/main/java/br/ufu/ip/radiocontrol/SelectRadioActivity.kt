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

        val radioList = resources.getStringArray(R.array.radios)
        val listAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_single_choice, radioList)
        listView.adapter = listAdapter
        listView.choiceMode = AbsListView.CHOICE_MODE_SINGLE
        val radio = intent.getStringExtra(EXTRA_STATION)
        if (radio != null) {
            val position = radioList.indexOf(radio)
            listView.setItemChecked(position, true)
        }
        listView.setOnItemClickListener {l, _, position, _ ->
            val result = l.getItemAtPosition(position) as String
            val it = Intent()
            it.putExtra(EXTRA_RESULT, result)
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }

    companion object {
        const val EXTRA_STATION = "station"
        const val EXTRA_RESULT = "result"
    }

}