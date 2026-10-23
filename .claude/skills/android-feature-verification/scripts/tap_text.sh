#!/usr/bin/env bash
# Taps the center of the first on-screen element whose text or content-desc equals $1.
# Usage: tap_text.sh "Electronics" [occurrence]   (occurrence is 1-based, default 1)
set -euo pipefail

label="$1"
occurrence="${2:-1}"

adb shell uiautomator dump /sdcard/ui.xml >/dev/null
# uiautomator escapes & as &amp; in the dump
escaped="${label//&/&amp;}"

bounds=$(adb shell cat /sdcard/ui.xml \
  | tr '>' '\n' \
  | grep -E "(text|content-desc)=\"$(printf '%s' "$escaped" | sed 's/[][\.*^$/]/\\&/g')\"" \
  | grep -oE 'bounds="\[[0-9]+,[0-9]+\]\[[0-9]+,[0-9]+\]"' \
  | sed -n "${occurrence}p" || true)

if [ -z "$bounds" ]; then
  echo "tap_text: no element with text \"$label\" on screen" >&2
  exit 1
fi

read -r x1 y1 x2 y2 <<<"$(echo "$bounds" | grep -oE '[0-9]+' | tr '\n' ' ')"
x=$(( (x1 + x2) / 2 ))
y=$(( (y1 + y2) / 2 ))
adb shell input tap "$x" "$y"
echo "tapped \"$label\" at $x,$y"
