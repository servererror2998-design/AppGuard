package com.servererror2998.appguard

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import android.os.SystemClock

class AppGuardAccessibilityService : AccessibilityService() {
    private var lastBlockedPackage: String? = null
    private var lastBrowserSeenAt: Long = 0L

    private val browserPackages = setOf(
        "com.android.chrome",
        "org.mozilla.firefox",
        "com.microsoft.emmx",
        "com.brave.browser",
        "com.sec.android.app.sbrowser",
        "com.opera.browser",
        "com.opera.mini.native",
        "com.duckduckgo.mobile.android",
        "com.vivaldi.browser"
    )

    private val blockedPackages = setOf(
        "com.shopee.id",
        "com.tiktok.android",
        "com.lazada.android",
        "com.whatsapp"
    )

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_WINDOWS_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 50
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val currentPackage = event.packageName?.toString() ?: return
        if (currentPackage == packageName) return

        val now = SystemClock.elapsedRealtime()
        if (currentPackage in browserPackages) {
            lastBrowserSeenAt = now
        }

        val browserRecentlyActive = now - lastBrowserSeenAt <= 5000L
        val globalPolicy = GlobalPolicyStore(applicationContext)

        if (globalPolicy.isBlocking() &&
            currentPackage in blockedPackages &&
            browserRecentlyActive &&
            currentPackage != lastBlockedPackage
        ) {
            lastBlockedPackage = currentPackage
            performGlobalAction(GLOBAL_ACTION_BACK)
            Toast.makeText(
                applicationContext,
                "AppGuard: pembukaan aplikasi diblokir",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (currentPackage !in blockedPackages) {
            lastBlockedPackage = null
        }
    }

    override fun onInterrupt() = Unit
}
