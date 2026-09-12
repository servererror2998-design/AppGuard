# AppGuard

Android app for controlling attempts by links/intents to open other apps.

## Goal

AppGuard is designed for a browsing scenario where a website may try to launch an external Android application (for example Shopee) unexpectedly. It does **not** block ads or website content.

## Global policy

The app uses one global rule for all external applications:

- **Izinkan Semua** — allow external-app opening.
- **Blokir Semua** — block external-app opening.

There is no per-application policy list and no separate Ask mode in this version.

## Important Android limitation

Android does not provide a normal third-party app with a universal API that can transparently intercept every `startActivity()` call made by every other app. AppGuard therefore uses supported intent/deep-link handling mechanisms and should be treated as a focused first version, not a kernel-level application firewall.
