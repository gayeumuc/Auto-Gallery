package com.nelinor.autoprop;

/**
 * Simple in-process singleton that holds the latest cropped bitmap.
 *
 * Because MainActivity and MediaCarAppService run in the same process,
 * this avoids all FileProvider / URI permission issues when sharing
 * a cropped bitmap from the phone UI to the car screen.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004R\"\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\r"}, d2 = {"Lcom/nelinor/autoprop/InMemoryBitmapStore;", "", "()V", "<set-?>", "Landroid/graphics/Bitmap;", "croppedBitmap", "getCroppedBitmap", "()Landroid/graphics/Bitmap;", "clear", "", "store", "", "bitmap", "app_debug"})
public final class InMemoryBitmapStore {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile android.graphics.Bitmap croppedBitmap;
    @org.jetbrains.annotations.NotNull()
    public static final com.nelinor.autoprop.InMemoryBitmapStore INSTANCE = null;
    
    private InMemoryBitmapStore() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap getCroppedBitmap() {
        return null;
    }
    
    /**
     * Store a new cropped bitmap. Returns a unique token so StateFlow always emits.
     */
    public final long store(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return 0L;
    }
    
    public final void clear() {
    }
}