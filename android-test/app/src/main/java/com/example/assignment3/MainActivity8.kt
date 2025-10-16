package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity8 : AppCompatActivity() {

    // ---- SharedPreferences keys ----
    private val PREFS_NAME = "auth_prefs"
    private val KEY_USERS_SET = "users_set"           // Set<String> of registered emails
    private val USER_PREFIX = "user_"                 // "user_<email>_password"
    private val KEY_IS_LOGGED_IN = "is_logged_in"     // optional session flag
    private val KEY_LOGGED_EMAIL = "logged_email"     // optional session email

    // Lazy prefs instance
    private val prefs by lazy { getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main8) // keep your existing layout reference

        val signupButton = findViewById<Button>(R.id.button2)       // "Sign in" button
        val loginBtn     = findViewById<TextView>(R.id.textView4)   // "Already have an account"
        val emailEt      = findViewById<EditText>(R.id.editTextText)
        val passwordEt   = findViewById<EditText>(R.id.editTextText2)
        val confirmPwEt  = findViewById<EditText>(R.id.editTextText3)
        val toggleTv     = findViewById<TextView>(R.id.textView3)   // "Show Password"

        // --- show/hide password toggle ---
        var passwordsVisible = false
        fun applyPasswordVisibility(visible: Boolean) {
            val method = if (visible)
                HideReturnsTransformationMethod.getInstance()
            else
                PasswordTransformationMethod.getInstance()

            passwordEt.transformationMethod = method
            confirmPwEt.transformationMethod = method

            // keep cursor at end
            passwordEt.setSelection(passwordEt.text?.length ?: 0)
            confirmPwEt.setSelection(confirmPwEt.text?.length ?: 0)

            toggleTv.text = if (visible) "Hide Password" else "Show Password"
        }
        applyPasswordVisibility(false)

        toggleTv.setOnClickListener {
            passwordsVisible = !passwordsVisible
            applyPasswordVisibility(passwordsVisible)
        }

        // --- Register ("Sign in") ---
        signupButton.setOnClickListener {
            val email    = emailEt.text?.toString()?.trim().orEmpty()
            val password = passwordEt.text?.toString()?.trim().orEmpty()
            val confirm  = confirmPwEt.text?.toString()?.trim().orEmpty()

            fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

            // Validations
            if (email.isEmpty()) {
                toast("Please enter your email")
                return@setOnClickListener
            }
            if (!isValidEmail(email)) {
                toast("Enter a valid email address")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                toast("Please enter a password")
                return@setOnClickListener
            }
            if (confirm.isEmpty()) {
                toast("Please confirm your password")
                return@setOnClickListener
            }

            val policyError = getPasswordPolicyError(password)
            if (policyError != null) {
                toast(policyError)
                return@setOnClickListener
            }

            if (password != confirm) {
                toast("Passwords do not match")
                return@setOnClickListener
            }

            // --- NEW: register to SharedPreferences ---
            if (isEmailTaken(email)) {
                toast("An account with this email already exists")
                return@setOnClickListener
            }

            saveUser(email, password) // persist locally
            signInAs(email)           // optional session flag

            toast("Account created successfully")
            startActivity(Intent(this, MainActivity7::class.java))
            // finish() // optional: uncomment to prevent going back to this screen
        }

        // "Already have an account" -> navigate to login screen
        loginBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity7::class.java))
        }
    }

    // ---- Helpers ----

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun getPasswordPolicyError(pw: String): String? {
        return when {
            pw.length < 6 -> "Password must be at least 6 characters"
            !pw.any { it.isUpperCase() } -> "Include at least one uppercase letter (A–Z)"
            !pw.any { !it.isLetterOrDigit() } -> "Include at least one special character (e.g. ! @ # $ %)"
            else -> null
        }
    }

    private fun isEmailTaken(email: String): Boolean {
        val users = prefs.getStringSet(KEY_USERS_SET, emptySet()) ?: emptySet()
        return email in users
    }

    private fun saveUser(email: String, password: String) {
        val users = (prefs.getStringSet(KEY_USERS_SET, emptySet()) ?: emptySet()).toMutableSet()
        users.add(email)

        prefs.edit()
            .putStringSet(KEY_USERS_SET, users)
            .putString("${USER_PREFIX}${email}_password", password)
            .apply()
    }

    private fun signInAs(email: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_LOGGED_EMAIL, email)
            .apply()
    }
}
