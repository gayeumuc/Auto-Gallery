package com.nelinor.autoprop.auto;

/**
 * Car screen that shows gallery images as a scrollable list.
 * Tapping an image updates [SharedImageState] and pushes [ImageDetailScreen].
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\bH\u0002R \u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/nelinor/autoprop/auto/ImageListScreen;", "Landroidx/car/app/Screen;", "carContext", "Landroidx/car/app/CarContext;", "(Landroidx/car/app/CarContext;)V", "imageItems", "", "Lkotlin/Pair;", "", "", "isLoading", "", "repository", "Lcom/nelinor/autoprop/repository/MediaRepository;", "loadImages", "", "onGetTemplate", "Landroidx/car/app/model/Template;", "onImageTapped", "id", "app_release"})
public final class ImageListScreen extends androidx.car.app.Screen {
    @org.jetbrains.annotations.NotNull()
    private final com.nelinor.autoprop.repository.MediaRepository repository = null;
    private boolean isLoading = true;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<kotlin.Pair<java.lang.Long, java.lang.String>> imageItems;
    
    public ImageListScreen(@org.jetbrains.annotations.NotNull()
    androidx.car.app.CarContext carContext) {
        super(null);
    }
    
    private final void loadImages() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.car.app.model.Template onGetTemplate() {
        return null;
    }
    
    private final void onImageTapped(long id) {
    }
}