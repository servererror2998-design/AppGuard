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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
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

enum class Policy { ALLOW, ASK, BLOCK }

data class ProtectedApp(
    val packageName: String,
    val label: String,
    val policy: Policy
)

private class PolicyStore(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("policies", Context.MODE_PRIVATE)

    fun get(packageName: String): Policy = runCatching {
        Policy.valueOf(prefs.getString(packageName, Policy.ASK.name) ?: Policy.ASK.name)
    }.getOrDefault(Policy.ASK)

    fun set(packageName: String, policy: Policy) {
        prefs.edit().putString(packageName, policy.name).apply()
    }
}

@androidx.compose.runtime.Composable
private fun AppGuardScreen(context: Context) {
    val store = remember { PolicyStore(context.applicationContext) }
    var apps by remember {
        mutableStateOf(
            listOf(
                ProtectedApp("com.shopee.id", "Shopee", store.get("com.shopee.id")),
                ProtectedApp("com.tiktok.android", "TikTok", store.get("com.tiktok.android")),
                ProtectedApp("com.lazada.android", "Lazada", store.get("com.lazada.android")),
                ProtectedApp("com.whatsapp", "WhatsApp", store.get("com.whatsapp"))
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("AppGuard", style = MaterialTheme.typography.headlineMedium)
        Text("Mengatur apakah deep-link boleh membuka aplikasi eksternal.")
        Text("Iklan dan isi halaman web tidak diblokir.", style = MaterialTheme.typography.bodySmall)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(apps, key = { it.packageName }) { app ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(app.label, style = MaterialTheme.typography.titleMedium)
                        Text(app.packageName, style = MaterialTheme.typography.bodySmall)
                        Text("Aturan: ${app.policy}")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                store.set(app.packageName, Policy.ALLOW)
                                apps = apps.map { if (it.packageName == app.packageName) it.copy(policy = Policy.ALLOW) else it }
                            }) { Text("Izinkan") }
                            OutlinedButton(onClick = {
                                store.set(app.packageName, Policy.ASK)
                                apps = apps.map { if (it.packageName == app.packageName) it.copy(policy = Policy.ASK) else it }
                            }) { Text("Tanya") }
                            OutlinedButton(onClick = {
                                store.set(app.packageName, Policy.BLOCK)
                                apps = apps.map { if (it.packageName == app.packageName) it.copy(policy = Policy.BLOCK) else it }
                            }) { Text("Tolak") }
                        }
                    }
                }
            }
        }
    }
}
