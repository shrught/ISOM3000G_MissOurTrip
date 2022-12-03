package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.missourtrip.databinding.ActivityHomeBinding
import com.example.missourtrip.databinding.ActivityRateBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCurrencyData().start()
    }

    private fun fetchCurrencyData(): Thread{
        return Thread{
            //you can try to go to this api yourself using this url
            val url = URL("https://open.er-api.com/v6/latest/hkd")
            val connection = url.openConnection() as HttpURLConnection

            if (connection.responseCode == 200){
                val inputSystem = connection.inputStream
                //println(inputSystem.toString())
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, Request::class.java)
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()
            }
            else{
                binding.baseCurrency.text = "Failed Connection"
            }
        }
    }

    private fun updateUI(request: Request) {
        runOnUiThread{
            kotlin.run{
                //to add more currency, first add to xml, then add to Currency.kt, then bind here
                binding.lastUpdated.text = request.time_last_update_utc
                binding.usd.text = String.format("USD: %.2f", request.rates.USD)
                binding.eur.text = String.format("EUR: %.2f", request.rates.EUR)
                binding.jpy.text = String.format("JPY: %.2f", request.rates.JPY)
                binding.gbp.text = String.format("GBP: %.2f", request.rates.GBP)
                binding.aud.text = String.format("AUD: %.2f", request.rates.AUD)
                binding.cad.text = String.format("CAD: %.2f", request.rates.CAD)
                binding.chf.text = String.format("CHF: %.2f", request.rates.CHF)
                binding.cny.text = String.format("CNH: %.2f", request.rates.CNY)
                binding.nzd.text = String.format("NZD: %.2f", request.rates.NZD)
            }
        }
    }
}