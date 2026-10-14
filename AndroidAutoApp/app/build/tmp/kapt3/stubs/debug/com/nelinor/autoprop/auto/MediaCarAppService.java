package com.nelinor.autoprop.auto;

/**
 * The entry point for the Android Auto side of the app.
 * Android Auto binds to this service when the phone is connected to a car head unit.
 *
 * IMPORTANT: androidx.car.app.minCarApiLevel must be declared as <meta-data>
 * inside this <service> tag in AndroidManifest.xml — not at the <application> level.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0007"}, d2 = {"Lcom/nelinor/autoprop/auto/MediaCarAppService;", "Landroidx/car/app/CarAppService;", "()V", "createHostValidator", "Landroidx/car/app/validation/HostValidator;", "onCreateSession", "Landroidx/car/app/Session;", "app_debug"})
public final class MediaCarAppService extends androidx.car.app.CarAppService {
    
    public MediaCarAppService() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.car.app.validation.HostValidator createHostValidator() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.car.app.Session onCreateSession() {
        return null;
    }
}