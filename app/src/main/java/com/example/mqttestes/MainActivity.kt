package com.example.mqttestes

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mqttestes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mqttClient = MQTTClient()



        //dado o fato de que a função connect é assíncrona, chamar a função subscribe logo após a connect
        //geraria um erro de nullPointerException. Para resolver isso, segundo a dev mailing list da paho,
        //é interessante subscrever no método onSucess, o que garante a conexão.

        binding.btnConectar.setOnClickListener {
            val topico = binding.inputTopico.text
            mqttClient.connect(this,topico.toString().uppercase())
        }



    }
}