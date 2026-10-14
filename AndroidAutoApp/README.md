# Auto Gallery — Android Auto Photo Viewer

An Android app that lets you **select a photo on your phone and instantly display it on the car screen** via Android Auto.

---

## Architecture

```
Phone UI (MainActivity)
    │
    │  user taps image
    ▼
SharedImageState  ◄──────────────────────────────────────────┐
    │  (StateFlow<Uri?>)                                      │
    │  emits new URI                                          │
    ▼                                                         │
Car Screens (Android Auto / Car App Library)                  │
    ├── GalleryHomeScreen   (home, shows "View Sent Photo")   │
    ├── ImageListScreen     (browse all gallery images)  ─────┘
    └── ImageDetailScreen   (full image display, live-updates)
```

### Key design decision — in-process singleton bridge

Both the phone `Activity` and the `CarAppService` run in the **same process**. `SharedImageState` is a Kotlin `object` (singleton) holding a `MutableStateFlow<Uri?>`. When the user taps a photo on the phone, `MainActivity` calls `SharedImageState.selectImage(...)`. The car's `ImageDetailScreen` collects that flow and calls `invalidate()` to redraw with the new image — no IPC, no broadcast receivers, no content providers needed.

---

## File structure

```
app/src/main/java/com/example/androidautoapp/
├── SharedImageState.kt          ← phone↔car bridge (StateFlow singleton)
├── MainActivity.kt              ← phone UI: grid of gallery images
├── adapter/
│   └── MediaAdapter.kt          ← RecyclerView adapter for image grid
├── model/
│   └── MediaItem.kt             ← data class for a gallery image
├── repository/
│   └── MediaRepository.kt       ← MediaStore queries
└── auto/
    ├── MediaCarAppService.kt    ← CarAppService entry point
    ├── MediaSession.kt          ← Car App Session
    ├── GalleryHomeScreen.kt     ← car home screen
    ├── ImageListScreen.kt       ← car image list (up to 6 items)
    └── ImageDetailScreen.kt     ← car full-image display (live-updating)
```

---

## How to build & run

### Prerequisites
- Android Studio Hedgehog or newer
- Android SDK 34
- A physical Android phone (API 26+) or emulator
- Android Auto Desktop Head Unit (DHU) for testing the car side

### Steps

1. Open the `AndroidAutoApp` folder in Android Studio.
2. Let Gradle sync finish.
3. Run the app on your phone (`▶ Run`).
4. Grant the **Photos / Media** permission when prompted.
5. Start the Android Auto DHU on your computer:
   ```
   start_dhu.bat   (Windows)
   ```
6. Connect your phone to the DHU via Android Auto.
7. Open **Auto Gallery** in the DHU launcher.
8. On the **phone**, tap any photo in the grid.
9. On the **car screen**, tap **"View Sent Photo"** — the image appears immediately.

---

## Android Auto constraints

The Car App Library (template-based) does **not** allow arbitrary custom Views on the car screen. Images are displayed as `CarIcon` bitmaps inside a `PaneTemplate`. The bitmap is downsampled to 800×480 px to fit car display constraints and avoid OOM errors.

The list screen is limited to **6 items** per `ListTemplate` page (Car App Library API 1 limit). For larger galleries, pagination with `ItemList` offsets would be needed.

---

## Permissions

| Permission | Why |
|---|---|
| `READ_MEDIA_IMAGES` (API 33+) | Read gallery images |
| `READ_EXTERNAL_STORAGE` (API ≤ 32) | Read gallery images (legacy) |
| `FOREGROUND_SERVICE` | Required for Car App Service |
| `FOREGROUND_SERVICE_MEDIA_PLAYBACK` | Required for Car App Service category |
