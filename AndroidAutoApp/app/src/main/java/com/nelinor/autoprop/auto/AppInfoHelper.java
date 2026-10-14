package com.nelinor.autoprop.auto;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Log;

/**
 * Helper that patches the minCarApiLevel meta-data in the service's Bundle
 * so that AppInfo.retrieveMinCarAppApiLevel() can read it via Bundle.getInt().
 *
 * Root cause: android:value="1" in XML is stored as a String in the Bundle,
 * but AppInfo uses Bundle.getInt() which returns -1 for String values.
 * This helper patches the Bundle to contain an actual int value.
 */
public class AppInfoHelper {

    private static final String TAG = "AppInfoHelper";
    private static final String MIN_API_KEY = "androidx.car.app.minCarApiLevel";

    public static void patchServiceMetaData(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ServiceInfo si = pm.getServiceInfo(
                    new android.content.ComponentName(context, MediaCarAppService.class),
                    PackageManager.GET_META_DATA
            );
            if (si.metaData == null) {
                si.metaData = new Bundle();
            }
            // Force the value as an actual int (not String)
            si.metaData.putInt(MIN_API_KEY, 1);
            Log.d(TAG, "Patched minCarApiLevel = " + si.metaData.getInt(MIN_API_KEY, -1));
        } catch (Exception e) {
            Log.e(TAG, "Failed to patch meta-data", e);
        }
    }
}
