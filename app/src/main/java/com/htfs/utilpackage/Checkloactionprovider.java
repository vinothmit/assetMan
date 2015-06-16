package com.htfs.utilpackage;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Vinoth on 20-05-2015.
 */
public class Checkloactionprovider {
    /**
     * Check if the location service is on
     *
     * @param context Called activity context
     * @return boolean
     */
    public static boolean checklocationService(Context context) {

        LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
