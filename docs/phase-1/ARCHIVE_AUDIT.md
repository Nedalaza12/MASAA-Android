# MASAA Phase 1 — Source Archive Audit

**Audit date:** 2026-11-20  
**Source:** `MASAA_v1.1_updated_project.zip` (166,265,403 bytes) and `MASAA_v1.1_COMPLETE_PROJECT_MAP.md`  
**Purpose:** Establish a verified preservation baseline before migration. This is an inventory, not a claim that the old app builds or passes tests.

## Verified archive facts

- 521 files total; 438 files under `app/src`.
- Native Android project using Kotlin and Jetpack Compose.
- Existing PDF reader: `PdfBookEngine.kt`, `ReaderScreen.kt`, and `ReaderDetailSheet.kt`.
- Interactive page data: 347 page JSON files plus four `_meta.json` files; 3,807 generated regions reported by the project map.
- Four source books are Grade 5 (English 58 pages, Arabic 109, Mathematics 88, Science 92). The archive contains 11 PDF copies representing only four unique PDFs.
- Existing application capabilities include navigation, home/progress, reader, local Room persistence, parental controls, games, AI tutor integration, Firebase/Auth/Firestore, App Check, billing/ads policy, curriculum registry, interactive-layer repository, and automated tests.
- The archive's own `docs/IMPLEMENTATION_REPORT.md` explicitly says APK build, Kotlin compilation/unit tests, and on-device tests were not performed in the environment that produced the archive. These must not be treated as passing.

## Important source-quality and security notes

- Grade 5 curriculum data and Grade 5 hotspots are not valid Grade 12 content. Do not copy them into the Grade 12 proof.
- The archive contains duplicate textbook PDFs in `app/src/main/assets`, `public/assets/aistudio`, and temporary AI Studio storage. Do not upload textbook PDFs to this public repository.
- The archive contains `app/google-services.json` and Firebase-related configuration. Review and replace/remove environment-specific configuration before any public source migration; never publish private service credentials, signing keys, tokens, or secrets.
- `_checkpoint/masaa-pre-update-checkpoint.tar.gz` is an internal backup, not a source of truth to publish.
- The archive's implementation report describes automatic region generation and its known coverage/geometry weaknesses. Existing regions require validation against the actual target PDF before reuse.

## Build environment check

- Java 21 is available in the current execution environment.
- `gradle` is not installed.
- `ANDROID_HOME` and `ANDROID_SDK_ROOT` are unset; Android SDK tools are not available.
- Therefore no Android build, emulator run, or device verification is claimed at this checkpoint.

## Phase 1 decision

Use the old code as a reference for reusable reader mechanics only. First isolate the PDF rendering, coordinate transforms, tap hit-testing, selection state, detail sheet, and page/zoom/pan restoration. Then build a clean Grade 12 proof with a target-grade PDF and hand-verified sample regions. Preserve the existing app's other capabilities in a separate migration matrix; do not silently delete or falsely claim migration of them.
