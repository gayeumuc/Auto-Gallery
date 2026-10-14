package com.nelinor.autoprop.repository;

/**
 * Queries the device MediaStore to retrieve images from the gallery.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u000bH\u0086@\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/nelinor/autoprop/repository/MediaRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getImageById", "Lcom/nelinor/autoprop/model/MediaItem;", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getImages", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class MediaRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    public MediaRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Returns all images from the device gallery, sorted by date taken (newest first).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getImages(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nelinor.autoprop.model.MediaItem>> $completion) {
        return null;
    }
    
    /**
     * Returns a single MediaItem by its ID, or null if not found.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getImageById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nelinor.autoprop.model.MediaItem> $completion) {
        return null;
    }
}