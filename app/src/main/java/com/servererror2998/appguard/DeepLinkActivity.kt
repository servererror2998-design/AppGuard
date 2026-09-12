package com.servererror2998.appguard

import android.app.Activity
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

        val blocking = GlobalPolicyStore(this).isBlocking()
        if (blocking) {
            Toast.makeText(
                this,
                "Pembukaan aplikasi eksternal diblokir",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "Pembukaan aplikasi eksternal diizinkan",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}
