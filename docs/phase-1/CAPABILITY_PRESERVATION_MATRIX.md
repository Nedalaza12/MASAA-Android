# MASAA — Capability Preservation Matrix

This matrix records capabilities found in the MASAA v1.1 Grade 5 archive and how they are treated during the Grade 12 Phase 1 proof. “Found” means present in source or project documentation, not build-verified.

| Capability | Evidence in archive | Phase 1 treatment | Verification still required |
|---|---|---|---|
| PDF page rendering | `PdfBookEngine.kt` | Reuse candidate; isolate behind reader interface | Correct page sizing, rendering quality, memory behavior |
| Interactive regions / hotspots | `InteractiveLayerRepository.kt`, `assets/interactive/*` | Reuse data model/loader only after schema review; discard Grade 5 content | Coordinate normalization, page-index convention, hit testing, overlap handling |
| Contextual detail sheet | `ReaderDetailSheet.kt` | Reuse presentation patterns where appropriate | Exact tapped-element identity and content mapping |
| Reader screen | `ReaderScreen.kt` | Reference implementation, not copied wholesale | Gesture arbitration, touch targets, state restoration |
| Page/zoom/pan persistence | Reader implementation and report | Must be preserved in proof | Return from detail to exact same page/zoom/position |
| Room local database | `MasaaDatabase.kt`, entities/DAOs | Out of scope for proof unless reader needs it | Schema, migrations, offline behavior |
| Student progress/bookmarks/notes | local data layer and screens | Preserve as existing product capability; not redesigned in proof | Persistence and backward compatibility |
| Home and progress dashboard | `HomeAndProgressScreens.kt` | Preserve for later migration | Navigation and progress accuracy |
| Parent controls / study timer | `ParentalControlsScreens.kt`, timer/domain files | Preserve for later migration | Parent gate, timer enforcement, accessibility |
| Games / mini-games | `GamesHubAndMiniGames.kt` | Preserve for later migration | Gameplay, navigation, age-appropriate behavior |
| AI tutor | `ChildSafeAiTutorRepository.kt`, tutor screens | Preserve as capability; not required for reader proof | Grounding, multi-turn behavior, offline fallback, safety |
| Firebase Auth / Firestore / App Check | remote layer and config | No backend changes in Phase 1 | Environment separation and rules review |
| Billing and ads policy | `PlayBillingAndAdsPolicy.kt`, `SafeAdsManager.kt` | Do not activate or change paid services/ads | Policy review and explicit authorization before activation |
| Curriculum registry | `CurriculumRegistry.kt`, Grade 5 curriculum classes | Replace Grade 5 registrations with target curriculum when authorized source is available | Subject/track isolation and exact curriculum metadata |
| Automated tests | `app/src/test`, `app/src/androidTest` | Preserve and adapt relevant tests | Run unit/instrumentation tests in a real Android build environment |
| Branding / Material 3 theme | `ui/theme/*`, resources | Preserve MASAA identity; adapt only when required by approved design contract | RTL, contrast, font rendering, device sizes |

## Non-negotiable preservation rules

1. No existing capability is considered removed merely because it is outside Phase 1. It remains recorded for migration planning.
2. Do not carry Grade 5 lesson text, questions, answer keys, PDFs, hotspots, or curriculum identifiers into Grade 12.
3. Do not upload full textbook PDFs, Firebase configuration, secrets, signing material, temporary AI Studio files, or backups to this public repository.
4. Do not report compilation, tests, APK generation, or device validation as successful until actually executed.
5. Each migrated capability must have a source path, destination path, migration status, and test evidence.
