package com.htfs.model;

import java.io.Serializable;

/**
 * Created by Vinoth on 26-05-2015.
 */
public class assetLocationHistory implements Serializable {

    private String assetID;
    private String assetUser;
    private String assetDesc;
    private String assetDateTime;
    private String assetLongitude;
    private String assetLatitude;
    private String assetLastAddress;
    private String assetCommnet;

    @Override
    public String toString() {
        return "assetLocationHistory{" +
                "assetID='" + assetID + '\'' +
                ", assetUser='" + assetUser + '\'' +
                ", assetDesc='" + assetDesc + '\'' +
                ", assetDateTime='" + assetDateTime + '\'' +
                ", assetLongitude='" + assetLongitude + '\'' +
                ", assetLatitude='" + assetLatitude + '\'' +
                ", assetLastAddress='" + assetLastAddress + '\'' +
                ", assetCommnet='" + assetCommnet + '\'' +
                '}';
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public String getAssetUser() {
        return assetUser;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
    }

    public String getAssetDesc() {
        return assetDesc;
    }

    public void setAssetDesc(String assetDesc) {
        this.assetDesc = assetDesc;
    }

    public String getAssetDateTime() {
        return assetDateTime;
    }

    public void setAssetDateTime(String assetDateTime) {
        this.assetDateTime = assetDateTime;
    }

    public String getAssetLongitude() {
        return assetLongitude;
    }

    public void setAssetLongitude(String assetLongitude) {
        this.assetLongitude = assetLongitude;
    }

    public String getAssetLatitude() {
        return assetLatitude;
    }

    public void setAssetLatitude(String assetLatitude) {
        this.assetLatitude = assetLatitude;
    }

    public String getAssetLastAddress() {
        return assetLastAddress;
    }

    public void setAssetLastAddress(String assetLastAddress) {
        this.assetLastAddress = assetLastAddress;
    }

    public String getAssetCommnet() {
        return assetCommnet;
    }

    public void setAssetCommnet(String assetCommnet) {
        this.assetCommnet = assetCommnet;
    }
}
