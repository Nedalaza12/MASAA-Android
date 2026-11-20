# Reader Extraction Map — MASAA v1.1

This is a static source inspection of the v1.1 archive. It identifies candidate boundaries; it does not claim the old implementation compiles.

| Source file | Observed role | Extraction decision |
|---|---|---|
| `app/src/main/java/com/example/media/PdfBookEngine.kt` | Android `PdfRenderer` wrapper with bitmap caching and coroutine dispatch | Candidate for reuse after lifecycle, file-descriptor, cache-size, and cancellation review |
| `app/src/main/java/com/example/ui/screens/ReaderScreen.kt` | Compose reader, page navigation, zoom/pan gestures, overlay drawing, tap handling, bookmarks/notes and navigation | Do not transplant wholesale; split the page surface/gesture layer from Grade 5 navigation and app-specific state |
| `app/src/main/java/com/example/ui/screens/ReaderDetailSheet.kt` | Detailed contextual sheet with question/answer state, hints, validation, subject styling and progress dependencies | Reuse interaction patterns only; create a minimal proof detail panel that displays the exact selected element |
| `app/src/main/java/com/example/curriculum/InteractiveLayerRepository.kt` | Reads page JSON and maps data into domain models | Candidate parser/loader; schema must be narrowed and validated for stable element IDs and normalized coordinates |
| `app/src/main/java/com/example/domain/model/SubjectModels.kt` | Contains subject/curriculum/hotspot/question domain models | Extract only generic reader models; avoid carrying Grade 5 curriculum assumptions |
| `app/src/main/java/com/example/data/local/MasaaEntities.kt` | Room entities include persistence-related models | Not needed for the initial reader proof unless durable reading position is included |
| `app/src/main/java/com/example/ui/theme/*` | MASAA Material 3 theme, contrast guard, subject palettes and patterns | Reuse brand tokens/components selectively; avoid coupling the reader proof to subject-specific Grade 5 logic |

## Reader proof contract

1. The official PDF page is the primary surface; it remains visible and visually dominant.
2. A tap resolves to one stable element ID and exact normalized page rectangle.
3. The detail surface receives the selected element object itself; it must not infer a nearby question or show generic content for an unmatched region.
4. Closing the detail surface restores the same PDF page, zoom, and pan offset.
5. Page changes, orientation/window changes, and zoom must not desynchronize the overlay from the rendered PDF page.
6. Touching the PDF surface must not accidentally trigger a region when the user is panning or pinching.
7. Missing or unverified element content must be labelled unavailable/unverified, not fabricated.
8. Page index convention (zero-based PDF index vs printed page number) must be explicit in data and tests.

## Known coupling to remove from the proof

- Grade 5 subject registry and curriculum IDs.
- Grade 5 question banks, answer keys, and interactive JSON.
- Grade 5 PDF assets.
- Firebase/Auth/Firestore, App Check, ads, billing, games, parental dashboard, and AI tutor dependencies unless the reader proof demonstrates a concrete need. Phase 1 does not authorize backend or billing changes.

## Required tests

- PDF page-to-view coordinate transform at fit-width and multiple zoom levels.
- Tap center and edge of a region; tap outside all regions.
- Two adjacent regions with shared/nearby edges.
- Pan and pinch do not dispatch a tap.
- Detail open/close restores page, zoom, and pan exactly.
- Page change clears or rebinds selected element correctly.
- Rotation/window resize preserves valid reading state and overlay alignment.
- Unknown element ID, malformed JSON, out-of-range coordinates, and missing content fail safely.
