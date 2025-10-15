package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment3.notifications.NotificationHelper
import com.example.assignment3.ui.settings.SettingsFragment
import com.example.assignment3.util.ReminderScheduler
import com.google.android.material.appbar.MaterialToolbar

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        NotificationHelper.ensureChannel(this)
        ReminderScheduler.ensureScheduled(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    com.example.assignment3.ui.habits.list.HabitListFragment()
                )
                .commit()
        }

        val btn1 = findViewById<Button>(R.id.btnAct)
        btn1.setOnClickListener {
            supportActionBar?.title = ""
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    com.example.assignment3.ui.habits.list.HabitListFragment()
                )
                .addToBackStack("mood")
                .commit()
        }

        val btn2 = findViewById<Button>(R.id.btnMood)
        btn2.setOnClickListener {
            supportActionBar?.title = ""
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    com.example.assignment3.ui.mood.MoodListFragment()
                )
                .addToBackStack("mood")
                .commit()
        }

        val btn3 = findViewById<Button>(R.id.btnSettings)
        btn3.setOnClickListener {
            supportActionBar?.title = ""
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    com.example.assignment3.ui.settings.SettingsFragment()
                )
                .addToBackStack("mood")
                .commit()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.logout -> {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
