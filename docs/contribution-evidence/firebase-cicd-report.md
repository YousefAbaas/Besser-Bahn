# CI/CD Contribution — Firebase App Distribution Pipeline

Branch: feat/network-resilience-pr (exploration branch)
Date: 2026-09-02

## What was done
Set up a GitHub Actions CI/CD pipeline that authenticates with Firebase
and automatically uploads a built APK to Firebase App Distribution for
beta testers.

## Debugging process
- Initial Firebase CLI + OIDC authentication attempts failed (runs #16, #17)
- Diagnosed and fixed the authentication flow (run #18)
- Successfully uploaded APK to Firebase App Distribution (run #19)
- Fixed a remaining upload-wait step issue (run #20 to #21)

## Evidence
See screenshots/firebase-cicd-runs.jpg
(Original workflow run logs on GitHub expire after 90 days on free
accounts — this file is the permanent record.)

## Note
This CI/CD work is separate from an earlier contribution already
merged into this project (session-expiry / refresh-token fix), and
separate from a later code change on the same exploration branch that
the maintainer did not accept due to a regression.
