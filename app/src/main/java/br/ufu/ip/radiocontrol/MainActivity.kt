package br.ufu.ip.radiocontrol

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import br.ufu.ip.radiocontrol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var station: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        if (savedInstanceState != null) {
            station = savedInstanceState.getString(EXTRA_STATION)
            if (station != null) {
                Log.d("MainActivity", station!!)
            }
        }
        val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                station = result.data?.getStringExtra(SelectRadioActivity.EXTRA_RESULT)
                Log.d("MainActivity", station!!)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(EXTRA_STATION, station)
    }

    companion object {
        const val EXTRA_STATION = "estado"
    }

}