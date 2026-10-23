---
name: android-feature-verification
description: Verifies that a feature in this Android app actually works - compile, unit tests and lint, then install on a connected emulator and drive the flow with adb (tap by label, screenshots, network off, rotation, back), and scan logcat for crashes and jank. Use when asked to check a feature works, or as part of a code review.
---

# Feature verification

Run the steps in order and report each one as pass, fail (with the first error line) or skipped (with
the reason). Never skip a step silently. Put screenshots and other scratch files in a temp directory,
not in the repo.

## 1. Build, tests, lint

```bash
./gradlew :app:compileDebugKotlin --console=plain
./gradlew testDebugUnitTest --console=plain
./gradlew lintDebug --console=plain
```

- Lint writes `app/build/reports/lint-results-debug.*`. Report only issues in files in scope.
- The project has only template tests. New logic (ViewModels, mappers, use cases) without tests is
  a review finding, not a verification failure.

## 2. Install and launch

```bash
adb devices                                 # need one line ending in "device"
./gradlew :app:installDebug --console=plain
adb logcat -c
adb shell am force-stop com.route.firstapp
adb shell am start -n com.route.firstapp/.MainActivity
```

If no device is listed, run `~/Library/Android/sdk/emulator/emulator -list-avds`, report the runtime
checks as skipped, and ask before starting an emulator. Booting one takes minutes.

## 3. Drive the app

- **Tap by label** with `.claude/skills/android-feature-verification/scripts/tap_text.sh "<text>" [occurrence]`.
  It reads the Compose UI through `uiautomator dump` and taps the center of the element whose text
  or content description matches. Prefer it to hard-coded coordinates.
- Bottom tabs have no labels. Get the screen size from `adb shell wm size` and tap the bar near the
  bottom edge: Home, Categories, Favorites and Profile split the width into four equal parts.
- See what's on screen:
  ```bash
  adb shell uiautomator dump /sdcard/ui.xml >/dev/null && adb shell cat /sdcard/ui.xml | tr '>' '\n' | grep -oE '(text|content-desc)="[^"]+"'
  adb exec-out screencap -p > "$TMPDIR/step.png"     # then look at the image
  ```
- Back: `adb shell input keyevent KEYCODE_BACK`. Wait 2–3 s after each action for network loads.

## 4. What to exercise

Cover what applies to the feature under review:

| Scenario | How |
|---|---|
| Happy path | Data loads, lists render, taps navigate to the right destination |
| Empty state | A case with no data (for example the Music category has no subcategories) |
| Error + retry | `adb shell svc wifi disable; adb shell svc data disable`, trigger the load, check the error UI, then `enable` both and tap retry |
| Back navigation | Returns to the previous screen with its state kept |
| Tab switching | Leaving and returning to a tab keeps its state |
| Rotation | `adb shell settings put system accelerometer_rotation 0; adb shell settings put system user_rotation 1`, check state survives, then `user_rotation 0` |
| Fast input | Double-tap a navigating item (no double navigation); switch selection quickly (no stale results) |

Always restore network and rotation settings before you finish.

## 5. Crash and jank scan

```bash
adb logcat -d | grep -E "FATAL EXCEPTION|AndroidRuntime|ANR in"
adb logcat -d | grep -E "Choreographer.*Skipped [0-9]+ frames"
```

A crash or ANR fails verification. Many skipped frames point to main-thread work and belong in the
performance findings.

## 6. Report

| Step | Result | Evidence |
|---|---|---|
| Compile / tests / lint | pass, fail or skipped | first error line, or the lint issues in scope |
| Scenario name | pass or fail | what you saw, screenshot path |
