package com.nelinor.autoprop.auto;

/**
 * The Car App Session. Created once per connection to the car head unit.
 * Returns [GalleryHomeScreen] as the initial screen shown on the car display.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0007"}, d2 = {"Lcom/nelinor/autoprop/auto/MediaSession;", "Landroidx/car/app/Session;", "()V", "onCreateScreen", "Landroidx/car/app/Screen;", "intent", "Landroid/content/Intent;", "app_release"})
public final class MediaSession extends androidx.car.app.Session {
    
    public MediaSession() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.car.app.Screen onCreateScreen(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
        return null;
    }
}