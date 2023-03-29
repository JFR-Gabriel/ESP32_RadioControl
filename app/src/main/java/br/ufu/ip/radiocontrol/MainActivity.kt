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

    private var station: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // create a function for load data from internal memory

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

        binding.btnSendConfigurations.setOnClickListener {
            // listener of button 'send configurations'
            /**
             * Create a socket, get the radio station frequency
             * and send this to the socket and wait the configuration
             * of the ESP32
             * */
        }

        binding.btnSaveToMemory.setOnClickListener {
            /**
             * Save all the configuration (last radio station used,
             * ip address, port number, etc) into the memory
             *
             * */
        }

        // talves depois possibilitar ao usuario adicionar novas radios

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(EXTRA_STATION, station)
    }

    companion object {
        const val EXTRA_STATION = "estado"
    }

}