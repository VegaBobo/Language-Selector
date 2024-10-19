### Language Selector

Language Selector allows users to set individual app languages. It tries to replicates the behavior of the "App languages" feature introduced in Android 13.

To use this app:
- MUST be on Android 13 or higher, there is no compatiblity with older Android versions.
- MUST have Shizuku.

You can get this app at Releases section.

<div>
<img src="https://raw.githubusercontent.com/VegaBobo/Language-Selector/main/other/preview_1.jpg" alt="preview" width="200"/>
<img src="https://raw.githubusercontent.com/VegaBobo/Language-Selector/main/other/preview_2.jpg" alt="preview" width="200"/>
</div>

### Features

- Set individual app languages
- Allows selecting language from any app **
- Quick change languages with QSTile

** Language Selector DOES NOT translate apps, it just specify a locale that will be used by application, if the desired language is supported by the app, it should be displayed as expected.

** Please note that changing locale for unsupported applications and system apps may cause unexpected behavior and is NOT RECOMMENDED.

#### Language availability

This app parses Locale (java.util.Locale) from Locale.getAvailableLocales(), consequently, numerous locales are present in the app, the language list is huge, if someone want to improve that, feel free to send a PR, because this way is pretty slow and languages aren't filtered accurately.

###  Usage

Before using this app, you MUST install and start Shizuku, the way this app works makes Shizuku MANDATORY, after that, you should follow this steps:

1. Install "Language Selector" (check Releases)
2. Open, grant Shizuku permissions and tap on "Proceed"
3. Choose a app you want to select it's language.
4. Select any language from list
5. That is it?

#### Pinning languages

You can pin languages by long-pressing on desired language, pinned languages will appear at the top of the list and will also be available in the QS tile.

#### Quick tile

You can quick change current running app language by adding a QS tile, available tile languages are the pinned ones, if no pinned language is set, then tile will be marked as Unavailable, changing system apps language from QS is also not supported.

### Background

I've made this app because MIUI doesn't seem to have app languages in Android 13 (at least on my device, running global MIUI 14/Android 13), by not having the feature, i mean, there is no option inside Settings app to change app languages individually, but since it is as Android 13 build,  there is a high change that locale service is still present, if so, we can use LocaleManager to do per-app basis locale operations.

Locale manager can be acessible via ADB, using "cmd locale" command, since adb has the ability to change other app languages, i've decided to make my own "front-end" for managing application locales, so i can set languages and use this feature, even if there is no UI for app languages in stock Settings app yet.

Since ADB is required to manage other application languages, this app uses Shizuku to interact with LocaleManager APIs at privileged level, that's why Shizuku is mandatory to use this app.

If your device is running Android 13 or higher, and your ROM doesn't include any option related to the app languages, this app may be useful.
