package com.example.implicitintent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnKirimPesan = findViewById<Button>(R.id.btnKirimPesan)
        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        val btnSetTimer = findViewById<Button>(R.id.btnSetTimer)
        val etURL = findViewById<EditText>(R.id.etURL)
        val btnOpenURL = findViewById<Button>(R.id.btnOpenURL)

        btnKirimPesan.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra("address", "0811234")
                putExtra("sms_body", "ISI SMS")
                type = "text/plain"
            }

            if (sendIntent.resolveActivity(packageManager) != null) {
                startActivity(
                    Intent.createChooser(
                        sendIntent,
                        "PILIH APLIKASI"
                    )
                )
            }
        }

        btnSetAlarm.setOnClickListener {
            val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "COBA ALARM")
                putExtra(AlarmClock.EXTRA_HOUR, 20)
                putExtra(AlarmClock.EXTRA_MINUTES, 15)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            }

            startActivity(alarmIntent)
        }

        btnSetTimer.setOnClickListener {
            val timerIntent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "COBA ALARM")
                putExtra(AlarmClock.EXTRA_LENGTH, 20)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            }

            startActivity(timerIntent)
        }

        btnOpenURL.setOnClickListener {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://" + etURL.text.toString())
            )

            if (webIntent.resolveActivity(packageManager) != null) {
                startActivity(webIntent)
            } else {
                Toast.makeText(
                    this,
                    "Tidak ada Aplikasi Browser ditemukan",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}