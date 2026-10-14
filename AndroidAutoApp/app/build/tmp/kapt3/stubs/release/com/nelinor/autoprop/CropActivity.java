package com.nelinor.autoprop;

/**
 * Phone-side crop/zoom editor.
 *
 * Receives either:
 *  - EXTRA_IMAGE_URL  (String) — remote URL to download
 *  - EXTRA_IMAGE_URI  (String) — local content:// URI
 *
 * The user pans/zooms the image inside a 16:9 crop window.
 * "Send to Car" crops the bitmap and calls SharedImageState.selectBitmap().
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0012\u0010\u0011\u001a\u00020\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\b\u0010\u0014\u001a\u00020\rH\u0002J\b\u0010\u0015\u001a\u00020\rH\u0002J\u0010\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/nelinor/autoprop/CropActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/nelinor/autoprop/databinding/ActivityCropBinding;", "currentBitmap", "Landroid/graphics/Bitmap;", "rotationDegrees", "", "downloadBitmap", "url", "", "loadFromUri", "", "uri", "Landroid/net/Uri;", "loadFromUrl", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "rotateCurrentBitmap", "sendCroppedTocar", "showLoading", "show", "", "Companion", "app_release"})
public final class CropActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AutoGallery";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_IMAGE_URL = "crop_image_url";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_IMAGE_URI = "crop_image_uri";
    private com.nelinor.autoprop.databinding.ActivityCropBinding binding;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Bitmap currentBitmap;
    private int rotationDegrees = 0;
    @org.jetbrains.annotations.NotNull()
    public static final com.nelinor.autoprop.CropActivity.Companion Companion = null;
    
    public CropActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadFromUrl(java.lang.String url) {
    }
    
    private final void loadFromUri(android.net.Uri uri) {
    }
    
    private final void rotateCurrentBitmap() {
    }
    
    private final void sendCroppedTocar() {
    }
    
    private final void showLoading(boolean show) {
    }
    
    private final android.graphics.Bitmap downloadBitmap(java.lang.String url) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/nelinor/autoprop/CropActivity$Companion;", "", "()V", "EXTRA_IMAGE_URI", "", "EXTRA_IMAGE_URL", "TAG", "startWithUri", "", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "startWithUrl", "url", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void startWithUrl(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String url) {
        }
        
        public final void startWithUri(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        android.net.Uri uri) {
        }
    }
}