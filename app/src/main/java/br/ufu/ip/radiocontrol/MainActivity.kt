package br.ufu.ip.radiocontrol

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import br.ufu.ip.radiocontrol.databinding.ActivityMainBinding
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var station: String? = null

    val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            station = result.data?.getStringExtra(SelectRadioActivity.EXTRA_RESULT)
            Log.d("MainActivity", station!!)
        }
    }

    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private lateinit var words: String
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://www.radios.com.br/radio/cidade/uberlandia/9882").get()
                for (element in document.getElementsByTag("h3")) {
                    var text = element.text()
                    var text_lower = text.lowercase()
                    if (text_lower.length > 8) {
                        var vl =  (text_lower.substring(
                            text_lower.length - 8, text_lower.length - 2
                        ))
                        var clear_tx = vl.replace(" ", "")
                        if (text_lower.contains("fm") && vl.contains(".")) {
                            println(clear_tx)
                        }
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        WebScratch().execute()
        // create a function for load data from internal memory

        if (savedInstanceState != null) {
            station = savedInstanceState.getString(EXTRA_STATION)
            if (station != null) {
                Log.d("MainActivity", station!!)
            }
        }

        binding.buttonSwitchStation.setOnClickListener {
            /** create an intent with 'SelectRadioActivity' and add this into register
             *  and launch this for start this activity
            * */
            var intent: Intent = Intent(this, SelectRadioActivity::class.java)
            register.launch(intent)
        }

        binding.btnSendConfigurations.setOnClickListener {
            // listener of button 'send configurations'
            /**
             * Create a socket, get the radio station frequency
             * and send this to the socket and wait the configuration
             * of the ESP32
             * */
            var endIP = binding.edtEndIp.text.toString()
            var portn = binding.edtPort.text.toString()
            if (endIP.isNotEmpty() && portn.isNotEmpty()) {
                try {
                    var socket = Socket(endIP,Integer.parseInt(portn))
                    var station_frequency = binding.textviewStation.text
                        .split('-')[1].replace(" ","").substring(1,3)
                    var datat = station_frequency.encodeToByteArray()
                    socket.getOutputStream().write(datat);
                    socket.getOutputStream().flush()
                    socket.close()
                    Toast.makeText(this, resources.getString(R.string.text_station_send),
                        Toast.LENGTH_LONG).show()
                }catch (e: IOException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                } catch (s: SecurityException) {
                    Toast.makeText(this, s.message, Toast.LENGTH_LONG).show()
                } catch (i: IllegalArgumentException) {
                    Toast.makeText(this, i.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnSaveToMemory.setOnClickListener {
            /**
             * Save all the configuration (last radio station used,
             * ip address, port number, etc) into the memory
             *
             * */
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