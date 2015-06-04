package com.htfs.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.htfs.assettracking.AssetTracking;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vinoth on 21-05-2015.
 */
public class GetaddressService extends IntentService {
    public static final String PRAM_LONG = "longitude";
    public static final String PRAM_LAT = "latitude";
    public static final String ADDR = "address";
    private String locationAddress = "";

    public GetaddressService() {
        super(GetaddressService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double _astLat = intent.getDoubleExtra(PRAM_LAT, 0.0);
        double _astLong = intent.getDoubleExtra(PRAM_LONG, 0.0);
        Log.d("Addresss Intent values", String.valueOf(_astLat) + "" + String.valueOf(_astLong));
        String addr = getAddressforLocation(_astLat, _astLong);
        Log.d("Address Intent Answer", addr);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(AssetTracking.ResponseReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(ADDR, addr);
        sendBroadcast(broadcastIntent);
    }

    private String getAddressforLocation(double _latitute, double _longitude) {
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geoCoder.getFromLocation(_latitute, _longitude, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                Log.d("Address List", listAddresses.toString());
                locationAddress = listAddresses.get(0).getAddressLine(0) + " " +
                        listAddresses.get(0).getAddressLine(1) + " " +
                        listAddresses.get(0).getCountryName();
                return locationAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "";
    }
}
