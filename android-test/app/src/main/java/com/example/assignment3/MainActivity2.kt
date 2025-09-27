package com.example.assignment3

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment3.notifications.NotificationHelper
import com.example.assignment3.ui.settings.SettingsFragment
import com.example.assignment3.util.ReminderScheduler
import com.google.android.material.appbar.MaterialToolbar

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If you created a new layout for this activity, use activity_main2.
        // If you want to reuse the old one, keep R.layout.activity_main.
        setContentView(R.layout.activity_main2)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        // ensure channel + schedule at app start (respects prefs)
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_habits -> {
                supportActionBar?.title = getString(R.string.activities)
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        com.example.assignment3.ui.habits.list.HabitListFragment()
                    )
                    .commit()
                true
            }
            R.id.menu_mood -> {
                supportActionBar?.title = getString(R.string.mood_journal)
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        com.example.assignment3.ui.mood.MoodListFragment()
                    )
                    .commit()
                true
            }
            R.id.menu_settings -> {
                supportActionBar?.title = "Settings"
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SettingsFragment())
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
