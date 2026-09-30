package com.example.implicitintent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import java.util.Calendar
import java.util.TimeZone


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnKirimPesan = findViewById<Button>(R.id.btnKirimPesan)
        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        val btnSetTimer = findViewById<Button>(R.id.btnSetTimer)
        val etURL = findViewById<EditText>(R.id.etURL)
        val btnOpenURL = findViewById<Button>(R.id.btnOpenURL)
        val btnSetEvent = findViewById<Button>(R.id.btnSetEvent)
        val btnGetPhoto = findViewById<Button>(R.id.btnGetPhoto)
        val ivHasil = findViewById<ImageView>(R.id.ivHasil)

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

        btnSetEvent.setOnClickListener {
            val calendar =
                Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val timePickerDialog = TimePickerDialog(
                        this,
                        { _, selectedHour, selectedMinute ->

                            val selectedDateTime = Calendar.getInstance().apply {
                                set(
                                    selectedYear,
                                    selectedMonth,
                                    selectedDay,
                                    selectedHour,
                                    selectedMinute
                                )
                            }

                            val endTime = selectedDateTime.clone() as Calendar
                            endTime.add(Calendar.HOUR_OF_DAY, 1)

                            val eventIntent = Intent(Intent.ACTION_INSERT).apply {
                                data = CalendarContract.Events.CONTENT_URI
                                putExtra(CalendarContract.Events.TITLE, "Meeting")
                                putExtra(CalendarContract.Events.EVENT_LOCATION, "Kantor")
                                putExtra(CalendarContract.Events.DESCRIPTION, "Deskripsi Meeting")
                                putExtra(CalendarContract.Events.ALL_DAY, false)
                                putExtra(
                                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                    selectedDateTime.timeInMillis
                                )
                                putExtra(
                                    CalendarContract.EXTRA_EVENT_END_TIME,
                                    endTime.timeInMillis
                                )
                            }

                            startActivity(eventIntent)
                        },
                        hour,
                        minute,
                        true
                    )

                    timePickerDialog.show()
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            if (bitmap != null) {
                ivHasil.setImageBitmap(bitmap)
            }
        }

        btnGetPhoto.setOnClickListener {
            cameraLauncher.launch(null)
        }
    }
}