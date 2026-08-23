# Pypet

Pypet is an epilepsy-conscious virtual pet and hands-on Python learning game for Android.

The Android application lives entirely under `android/` and is independent of the separate `Chat-gpt-epilepsy-filters` repository.

## Goals

- Learn the Python language through play and pet care.
- Run real Python exercises on-device.
- Progress from beginner syntax through advanced Python topics.
- Use calm, non-strobing, low-sensory presentation by design.
- Build toward real projects rather than only quizzes.

## Android

See [`android/`](android/) for the Android application and CI build.

The app embeds Python using Chaquopy. Chaquopy is open source and integrates Python with Android's standard Gradle build system. Python 3.13 is used for the initial app runtime.

Safety note: the visual design is intended as risk reduction for photosensitive users; it cannot guarantee seizure prevention and is not a medical device.
