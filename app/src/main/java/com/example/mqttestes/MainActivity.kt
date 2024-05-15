package com.example.mqttestes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mqttestes.databinding.ActivityMainBinding

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


        //mqttClient.connect(this, TOPICO)


        listen.observe(this, Observer {
            binding.btnConectar.text = "Desconectar"
        })



        binding.btnConectar.setOnClickListener {
            if (!testarConexao()) {
                mqttClient.connect(this, TOPICO)
            } else {
                mqttClient.disconnect(this)
            }
        }

        //MQTTClient().mudarCorExterna(::botaoConectado)


    }


    fun botaoConectado(cor: Int) {
        binding.btnConectar.setBackgroundColor(cor)
        binding.btnConectar.text = "Desconectar"
        Toast.makeText(this, "Conectado e subscrito!!", Toast.LENGTH_SHORT).show()

    }

    fun botaoDesconectado(cor: Int = 0xFFFF0000.toInt()) {
        binding.btnConectar.setBackgroundColor(cor)
        binding.btnConectar.text = "Conectar"
        Toast.makeText(this, "Desconectado do broker!", Toast.LENGTH_SHORT).show()
    }

    private fun testarConexao(): Boolean {

        var conectado = false

        if (mqttClient.isConnected()) {
            //Toast.makeText(this, "CONECTADO!", Toast.LENGTH_SHORT).show()
            conectado = true
        } else {
            //Toast.makeText(this, "NÃO CONECTADO!!", Toast.LENGTH_SHORT).show()
        }
    return conectado
    }
}



