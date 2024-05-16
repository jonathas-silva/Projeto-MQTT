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
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    companion object {

        const val TOPICO = "JONATHAS/TESTE"

    }

    private var listaDeMensagens = ""

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

        binding.btnEnviar.isEnabled = false

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

        binding.btnEnviar.setOnClickListener {
            val mensagem = binding.inputTopico.text.toString()
            mqttClient.publish(TOPICO, mensagem)
            binding.inputTopico.setText("")
        }


    }


    fun botaoConectado(cor: Int) {
        binding.btnConectar.setBackgroundColor(cor)
        binding.btnConectar.text = "Desconectar"
        Toast.makeText(this, "Conectado e subscrito!!", Toast.LENGTH_SHORT).show()
        binding.btnEnviar.isEnabled = true

    }

    fun botaoDesconectado(cor: Int = 0xFFFF0000.toInt()) {
        binding.btnConectar.setBackgroundColor(cor)
        binding.btnConectar.text = "Conectar"
        Toast.makeText(this, "Desconectado do broker!", Toast.LENGTH_SHORT).show()
        binding.btnEnviar.isEnabled = false
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


    fun receberMensagem(msg: String){

        /*
        Apenas concatena as mensagens com a mensagem da lista e atualiza o textView,
        que é zerado toda vez que o app reinicia (todo oncreate)
        */

        //Só vai concatenar a mensagem se começar com a string massa
        if(msg.startsWith("massa", true)){
            val formatted = horaAtualFormatada()

            val mensagemCompleta = "$formatted - $msg gramas\n"

            listaDeMensagens+=mensagemCompleta
            binding.textRecebidas.text = listaDeMensagens

            //Vamos estrair o valor da mensagem
            val numeroRegex = Regex("\\d+")

            val matchResult = numeroRegex.find(msg)
            val numeroString = matchResult?.value
            val numeroInteiro = numeroString?.toIntOrNull()
            val pesagemFormatada = numeroInteiro.toString() + "g"
            binding.txtPesagem.text = pesagemFormatada
            val infoAtualizado = "Atualizado em $formatted"
            binding.txtAtualizado.text = infoAtualizado
        }

    }

    private fun horaAtualFormatada(): String {
        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH).toString()
        val day = c.get(Calendar.DAY_OF_MONTH).toString()
        val hour = c.get(Calendar.HOUR_OF_DAY).toString()
        val minute = c.get(Calendar.MINUTE).toString()
        return "$day/$month $hour:$minute"
    }
}



