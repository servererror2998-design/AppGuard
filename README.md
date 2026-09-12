# AppGuard

Android app for controlling attempts by links/intents to open other apps.

## Goal

AppGuard is designed for a browsing scenario where a website may try to launch an external Android application (for example Shopee) unexpectedly. It does **not** block ads or website content.

## Important Android limitation

Android does not provide a normal third-party app with a universal API that can transparently intercept every `startActivity()` call made by every other app. AppGuard therefore uses supported intent/deep-link handling mechanisms and should be treated as a focused first version, not a kernel-level application firewall.

## Planned policy modes

- Allow
- Ask
- Block

The first version focuses on external-app/deep-link flows and a per-app policy list.
