package com.nelinor.autoprop;

/**
 * Singleton bridge between the phone UI and the Android Auto car screens.
 *
 * Supports two image sources:
 * 1. Local gallery image (selected via MediaStore) — stored as a content:// URI
 * 2. Remote image URL (typed/pasted by the user) — stored as an https:// URI
 *
 * Both the phone Activity and the CarAppService run in the same process,
 * so they share this in-memory singleton without any IPC overhead.
 *
 * Uses MutableSharedFlow for image events so that the same URI can be sent
 * multiple times and always triggers a reload (unlike StateFlow which deduplicates).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001)B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u001e\u001a\u00020\u001fJ\u0018\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\"2\b\b\u0002\u0010#\u001a\u00020\u001aJ\u001e\u0010$\u001a\u00020\u001f2\u0006\u0010%\u001a\u00020\b2\u0006\u0010&\u001a\u00020\n2\u0006\u0010#\u001a\u00020\u001aJ\u000e\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u001aR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\f8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0014R\u0019\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0018\u00a8\u0006*"}, d2 = {"Lcom/nelinor/autoprop/SharedImageState;", "", "()V", "_imageEvent", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/nelinor/autoprop/SharedImageState$ImageEvent;", "_selectedImageId", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_selectedImageUri", "Landroid/net/Uri;", "hasSelection", "", "getHasSelection", "()Z", "imageEvent", "Lkotlinx/coroutines/flow/SharedFlow;", "getImageEvent", "()Lkotlinx/coroutines/flow/SharedFlow;", "isRemoteUrl", "()Lkotlinx/coroutines/flow/MutableStateFlow;", "selectedImageId", "Lkotlinx/coroutines/flow/StateFlow;", "getSelectedImageId", "()Lkotlinx/coroutines/flow/StateFlow;", "selectedImageName", "", "getSelectedImageName", "selectedImageUri", "getSelectedImageUri", "clearSelection", "", "selectBitmap", "bitmap", "Landroid/graphics/Bitmap;", "name", "selectImage", "id", "uri", "selectUrl", "url", "ImageEvent", "app_release"})
public final class SharedImageState {
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableSharedFlow<com.nelinor.autoprop.SharedImageState.ImageEvent> _imageEvent = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.SharedFlow<com.nelinor.autoprop.SharedImageState.ImageEvent> imageEvent = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<android.net.Uri> _selectedImageUri = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<android.net.Uri> selectedImageUri = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _selectedImageId = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<java.lang.Long> selectedImageId = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> selectedImageName = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> isRemoteUrl = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.nelinor.autoprop.SharedImageState INSTANCE = null;
    
    private SharedImageState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<com.nelinor.autoprop.SharedImageState.ImageEvent> getImageEvent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<android.net.Uri> getSelectedImageUri() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getSelectedImageId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> getSelectedImageName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> isRemoteUrl() {
        return null;
    }
    
    /**
     * Called when the user taps a local gallery image.
     */
    public final void selectImage(long id, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    /**
     * Called when the user enters a remote image URL.
     */
    public final void selectUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    /**
     * Called when the user crops/edits a bitmap on the phone.
     *
     * Stores the bitmap in InMemoryBitmapStore (same process as the car service)
     * and emits a unique inmemory://crop/<timestamp> URI so StateFlow always fires
     * even when the same image is cropped multiple times.
     */
    public final void selectBitmap(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    /**
     * Clears the current selection.
     */
    public final void clearSelection() {
    }
    
    public final boolean getHasSelection() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0013\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\tH\u00c6\u0003J\t\u0010\u0018\u001a\u00020\tH\u00c6\u0003J;\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u001a\u001a\u00020\u00072\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001f"}, d2 = {"Lcom/nelinor/autoprop/SharedImageState$ImageEvent;", "", "uri", "Landroid/net/Uri;", "name", "", "isRemote", "", "id", "", "timestamp", "(Landroid/net/Uri;Ljava/lang/String;ZJJ)V", "getId", "()J", "()Z", "getName", "()Ljava/lang/String;", "getTimestamp", "getUri", "()Landroid/net/Uri;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_release"})
    public static final class ImageEvent {
        @org.jetbrains.annotations.NotNull()
        private final android.net.Uri uri = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String name = null;
        private final boolean isRemote = false;
        private final long id = 0L;
        private final long timestamp = 0L;
        
        public ImageEvent(@org.jetbrains.annotations.NotNull()
        android.net.Uri uri, @org.jetbrains.annotations.NotNull()
        java.lang.String name, boolean isRemote, long id, long timestamp) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.net.Uri getUri() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getName() {
            return null;
        }
        
        public final boolean isRemote() {
            return false;
        }
        
        public final long getId() {
            return 0L;
        }
        
        public final long getTimestamp() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.net.Uri component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        public final boolean component3() {
            return false;
        }
        
        public final long component4() {
            return 0L;
        }
        
        public final long component5() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.nelinor.autoprop.SharedImageState.ImageEvent copy(@org.jetbrains.annotations.NotNull()
        android.net.Uri uri, @org.jetbrains.annotations.NotNull()
        java.lang.String name, boolean isRemote, long id, long timestamp) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}