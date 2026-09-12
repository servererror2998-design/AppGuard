package com.servererror2998.appguard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
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

class GlobalPolicyStore(context: Context) {
    companion object {
        private const val PREFS_NAME = "appguard_settings"
        private const val KEY_BLOCK_EXTERNAL_APPS = "block_external_apps"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

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
        Text("Mencegah browser otomatis membuka aplikasi target.")
        Text(
            "Iklan dan halaman web tetap berjalan normal. AppGuard hanya menolak perpindahan dari browser ke aplikasi target.",
            style = MaterialTheme.typography.bodySmall
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("1. Aktifkan layanan AppGuard", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Buka Pengaturan Aksesibilitas, pilih AppGuard, lalu aktifkan. Ini wajib agar AppGuard dapat mendeteksi aplikasi yang dibuka browser."
                )
                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                ) {
                    Text("Buka Aksesibilitas")
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("2. Aturan blokir", style = MaterialTheme.typography.titleLarge)
                Text(
                    if (blocking) "STATUS: BLOKIR AKTIF" else "STATUS: BLOKIR NONAKTIF",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    if (blocking) {
                        "Chrome/Firefox dan browser yang didukung akan dicegah membuka Shopee, TikTok, Lazada, atau WhatsApp melalui perpindahan aplikasi."
                    } else {
                        "Pembukaan aplikasi target dibiarkan normal."
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
                        Text("Izinkan")
                    }

                    OutlinedButton(
                        onClick = {
                            store.setBlocking(true)
                            blocking = true
                        },
                        enabled = !blocking
                    ) {
                        Text("Blokir")
                    }
                }
            }
        }
    }
}
