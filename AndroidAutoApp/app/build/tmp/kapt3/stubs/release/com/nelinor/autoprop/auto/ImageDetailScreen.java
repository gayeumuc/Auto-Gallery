package com.nelinor.autoprop.auto;

/**
 * Car screen that displays the selected image using PaneTemplate + CarIcon.
 *
 * Category is now POI (not NAVIGATION), so Surface API is not available.
 * Instead we encode the bitmap as a CarIcon and display it via PaneTemplate,
 * which is the largest image display available in Car App Library 1.4.0
 * without Surface access.
 *
 * Max recommended bitmap size for CarIcon: 800×600 px.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000  2\u00020\u0001:\u0001 B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0002J \u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH\u0002J \u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH\u0002J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\nH\u0002J\n\u0010\u001b\u001a\u0004\u0018\u00010\u0006H\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0016J \u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/nelinor/autoprop/auto/ImageDetailScreen;", "Landroidx/car/app/Screen;", "carContext", "Landroidx/car/app/CarContext;", "(Landroidx/car/app/CarContext;)V", "currentBitmap", "Landroid/graphics/Bitmap;", "errorMessage", "", "isLoading", "", "calculateInSampleSize", "", "opts", "Landroid/graphics/BitmapFactory$Options;", "reqW", "reqH", "decodeSampledBitmapFromContent", "uri", "Landroid/net/Uri;", "maxW", "maxH", "downloadBitmap", "url", "loadBitmap", "", "isRemote", "loadDefaultBitmap", "onGetTemplate", "Landroidx/car/app/model/Template;", "scaleBitmap", "src", "Companion", "app_release"})
public final class ImageDetailScreen extends androidx.car.app.Screen {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AutoGallery";
    private static final int TARGET_WIDTH = 1000;
    private static final int TARGET_HEIGHT = 193;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Bitmap currentBitmap;
    private boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String errorMessage;
    @org.jetbrains.annotations.NotNull()
    public static final com.nelinor.autoprop.auto.ImageDetailScreen.Companion Companion = null;
    
    public ImageDetailScreen(@org.jetbrains.annotations.NotNull()
    androidx.car.app.CarContext carContext) {
        super(null);
    }
    
    private final void loadBitmap(android.net.Uri uri, boolean isRemote) {
    }
    
    /**
     * Load the bundled nature image from assets as a fallback bitmap.
     */
    private final android.graphics.Bitmap loadDefaultBitmap() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.car.app.model.Template onGetTemplate() {
        return null;
    }
    
    private final android.graphics.Bitmap downloadBitmap(java.lang.String url, int maxW, int maxH) {
        return null;
    }
    
    private final android.graphics.Bitmap decodeSampledBitmapFromContent(android.net.Uri uri, int maxW, int maxH) {
        return null;
    }
    
    /**
     * Scale and center-crop the bitmap to exactly [maxW]×[maxH] so it fills
     * the Tesla central display (16:10) without letterboxing.
     */
    private final android.graphics.Bitmap scaleBitmap(android.graphics.Bitmap src, int maxW, int maxH) {
        return null;
    }
    
    private final int calculateInSampleSize(android.graphics.BitmapFactory.Options opts, int reqW, int reqH) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/nelinor/autoprop/auto/ImageDetailScreen$Companion;", "", "()V", "TAG", "", "TARGET_HEIGHT", "", "TARGET_WIDTH", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}