package com.example.mqttestes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mqttestes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.MqttException

class MainActivity : AppCompatActivity() {

    companion object {

        const val TOPICO = "JONATHAS/TESTE"

    }


    private val mqttClient = MQTTClient()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val listen = MutableLiveData<Boolean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        //dado o fato de que a função connect é assíncrona, chamar a função subscribe logo após a connect
        //geraria um erro de nullPointerException. Para resolver isso, segundo a dev mailing list da paho,
        //é interessante subscrever no método onSucess, o que garante a conexão.


        mqttClient.connect(this, TOPICO)


        listen.observe(this, Observer {
            binding.btnConectar.text = "Desconectar"
        })


        //TESTA A CONEXÃO
        binding.btnConectar.setOnClickListener {

            var conectado = false

            if (mqttClient.isConnected()) {
                Toast.makeText(this, "CONECTADO!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "NÃO CONECTADO!!", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnMudarCor.setOnClickListener {
            val vermelho = 0xFFFF0000.toInt()
            mudarCorBotao(vermelho)
        }

    }

    private fun mudarCorBotao(cor: Int) {
        binding.btnConectar.setBackgroundColor(cor)
    }

}