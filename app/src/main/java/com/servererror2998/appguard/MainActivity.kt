package com.servererror2998.appguard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppGuardScreen(this)
            }
        }
    }
}

/** Global AppGuard policy. The setting applies to every external app launch. */
class GlobalPolicyStore(context: Context) {
    companion object {
        private const val PREFS_NAME = "appguard_settings"
        private const val KEY_BLOCK_EXTERNAL_APPS = "block_external_apps"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** false = allow all, true = block all. Default is allow all. */
    fun isBlocking(): Boolean = prefs.getBoolean(KEY_BLOCK_EXTERNAL_APPS, false)

    fun setBlocking(block: Boolean) {
        prefs.edit().putBoolean(KEY_BLOCK_EXTERNAL_APPS, block).apply()
    }
}

@androidx.compose.runtime.Composable
private fun AppGuardScreen(context: Context) {
    val store = remember { GlobalPolicyStore(context.applicationContext) }
    var blocking by remember { mutableStateOf(store.isBlocking()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("AppGuard", style = MaterialTheme.typography.headlineMedium)
        Text("Atur satu aturan untuk semua aplikasi eksternal.")
        Text(
            "Iklan dan isi halaman web tidak diblokir. AppGuard hanya mengatur pembukaan aplikasi eksternal melalui deep-link yang ditangani AppGuard.",
            style = MaterialTheme.typography.bodySmall
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Aturan global", style = MaterialTheme.typography.titleLarge)
                Text(
                    if (blocking) "STATUS: BLOKIR SEMUA" else "STATUS: IZINKAN SEMUA",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    if (blocking) {
                        "Pembukaan aplikasi eksternal akan ditolak oleh aturan AppGuard."
                    } else {
                        "Pembukaan aplikasi eksternal diizinkan oleh aturan AppGuard."
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            store.setBlocking(false)
                            blocking = false
                        },
                        enabled = blocking
                    ) {
                        Text("Izinkan Semua")
                    }

                    OutlinedButton(
                        onClick = {
                            store.setBlocking(true)
                            blocking = true
                        },
                        enabled = !blocking
                    ) {
                        Text("Blokir Semua")
                    }
                }
            }
        }
    }
}
