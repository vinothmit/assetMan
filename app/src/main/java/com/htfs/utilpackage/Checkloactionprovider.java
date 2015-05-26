package com.htfs.utilpackage;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Vinoth on 20-05-2015.
 */
public class Checkloactionprovider {

    public static boolean checklocationService(Context context) {

        LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled;
    }
}
