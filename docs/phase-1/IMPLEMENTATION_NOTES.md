# Phase 1 — Reader implementation notes

## Implemented in this branch
- Created a clean Kotlin/Jetpack Compose Android application module.
- Added a native Android `PdfRenderer` wrapper that opens a user-selected PDF through the Storage Access Framework, renders pages, and closes native resources.
- Added normalized page-region models and deterministic hit resolution. A tap selects only one uniquely matching region; zero matches and overlapping matches return no selection.
- Added a page-layer loader contract. Curated JSON overlays belong under `app/src/main/assets/interactive/<book-key>/p<1-based-page>.json` with an `elements` array; each item requires `id`, `title`, `explanation`, and normalized `bounds` (left/top/right/bottom).
- Added unit tests for exact hits, misses, wrong-page hits, and ambiguous overlaps.
- Verified the JVM tests and debug APK assembly in successful GitHub Actions run [44781042186](https://github.com/Nedalaza12/MASAA-Android/actions/runs/44781042186).

## Not yet implemented / not claimed
- No Grade 12 hotspot/region JSON has been authored or committed. Therefore taps do not yet show fabricated explanations.
- No official PDF is bundled. PDFs remain outside this public repository; the current reader accepts a locally available PDF selected by the learner.
- Full pan/zoom coordinate mapping, exact selection overlay visualization, curriculum catalogue integration, and complete student-facing navigation remain follow-up work.
- No emulator or physical-device verification has been performed.
- The workflow is being extended to upload the generated debug APK as a temporary artifact; APK availability must be confirmed from the new workflow run.

## Source reuse decision
The Grade 5 archive's `PdfBookEngine.kt`, `ReaderScreen.kt`, `ReaderDetailSheet.kt`, and `InteractiveLayerRepository.kt` were inspected. Their useful concepts informed this isolated implementation, but Grade 5 content, generated hotspots, and application-specific dependencies were not copied.
