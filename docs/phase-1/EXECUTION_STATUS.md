# Phase 1 Execution Status

**Updated:** 2026-11-20

## Completed

- Confirmed GitHub write access and isolated all work on `phase-1/textbook-reader-proof`; `main` remains untouched.
- Audited the Grade 5 archive and identified the old PDF engine, Compose reader, detail sheet, and interactive-layer repository as reference candidates only.
- Inspected the connected MASAA Supabase `public.educational_packages` catalogue and verified the official 2024 Grade 12 Scientific Mathematics PDF (128 pages) and English Learning Modules PDF (69 pages). Both official Ministry URLs returned HTTP 200. The PDFs are not committed to this public repository.
- Added a clean Kotlin/Jetpack Compose Android project foundation, a native `PdfRenderer` adapter, a PDF file picker, page navigation, basic zoom, normalized interactive-region models, exact-hit resolution, and a page JSON loader.
- Added unit tests for exact region hit, miss, wrong page, and overlapping/ambiguous regions.
- Added a GitHub Actions workflow to run JVM unit tests and assemble a debug APK using JDK 17 and Gradle 8.9.
- Added implementation notes describing the region JSON contract and outstanding work.

## Current implementation limits

- No Grade 12 region/hotspot JSON has been authored or committed. The reader intentionally does not invent explanations; until curated region data is supplied, tapping a page will not show a fabricated lesson explanation.
- No official PDF is bundled. The current reader opens a PDF selected from device storage; this keeps textbook files out of the public repository and permits local/offline reading after selection.
- Zoom is present, but full pan/zoom coordinate mapping, pan restoration after closing the detail panel, visible region outlines, PDF-page state persistence across process death, and integration with the existing MASAA home/progress/navigation remain unfinished.
- The interactive JSON contract uses 1-based file names (`p1.json`) and 0-based in-memory page indices. Region bounds are normalized to the original page.
- The legacy reader source was reviewed, but the new reader is not yet a complete replacement for the old application. Existing student progress, bookmarks, notes, parent controls, games, AI tutor, and other capabilities remain recorded in the preservation matrix and are not claimed as migrated.

## Build verification

A GitHub Actions workflow has been added, but no successful run has yet been observed. The local execution environment has Java 21 but no Gradle executable or Android SDK, so local Android compilation, tests, emulator/device verification, and APK production have not been performed or claimed.

## Next steps

1. Verify the first GitHub Actions run and correct any compilation/test failures.
2. Add a curated Grade 12 region dataset based on page-by-page inspection of the official PDFs, beginning with a small set of high-value examples.
3. Implement correct transformed-page hit testing for zoom and pan, region outlines, and full reading-state restoration.
4. Validate against the English 69-page and Mathematics 128-page PDFs, without committing the PDFs themselves.
5. Integrate the proven reader into the wider MASAA application while preserving the capabilities listed in `CAPABILITY_PRESERVATION_MATRIX.md`.
