package com.servererror2998.appguard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast

/**
 * Entry point for AppGuard-owned deep links.
 *
 * Android does not expose a universal interception hook for arbitrary intents
 * issued by every other app. This activity provides a safe, explicit deep-link
 * surface that can be expanded for supported schemes without inspecting or
 * modifying web traffic.
 */
class DeepLinkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri: Uri? = intent?.data
        val targetPackage = uri?.getQueryParameter("package")
        val policy = targetPackage?.let { PolicyStoreAdapter(this).get(it) } ?: Policy.ASK

        when (policy) {
            Policy.BLOCK -> Toast.makeText(this, "Pembukaan aplikasi diblokir", Toast.LENGTH_SHORT).show()
            Policy.ALLOW -> Toast.makeText(this, "Pembukaan aplikasi diizinkan", Toast.LENGTH_SHORT).show()
            Policy.ASK -> Toast.makeText(this, "Memerlukan konfirmasi", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}

private class PolicyStoreAdapter(private val activity: Activity) {
    private val prefs = activity.getSharedPreferences("policies", Activity.MODE_PRIVATE)
    fun get(packageName: String): Policy = runCatching {
        Policy.valueOf(prefs.getString(packageName, Policy.ASK.name) ?: Policy.ASK.name)
    }.getOrDefault(Policy.ASK)
}
