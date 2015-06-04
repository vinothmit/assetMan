package com.htfs.model;

import java.io.Serializable;

/**
 * Created by Vinoth on 26-05-2015.
 */
public class AssetLocationHistory implements Serializable {

    private String assetID;
    private String assetUser;
    private String assetDesc;
    private String assetDateTime;
    private String gps;
    private String assetLastAddress;
    private String assetComment;

    @Override
    public String toString() {
        return "AssetLocationHistory{" +
                "assetID='" + assetID + '\'' +
                ", assetUser='" + assetUser + '\'' +
                ", assetDesc='" + assetDesc + '\'' +
                ", assetDateTime='" + assetDateTime + '\'' +
                ", gps='" + gps + '\'' +
                ", assetLastAddress='" + assetLastAddress + '\'' +
                ", assetComment='" + assetComment + '\'' +
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

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getAssetLastAddress() {
        return assetLastAddress;
    }

    public void setAssetLastAddress(String assetLastAddress) {
        this.assetLastAddress = assetLastAddress;
    }

    public String getAssetComment() {
        return assetComment;
    }

    public void setAssetComment(String assetComment) {
        this.assetComment = assetComment;
    }
}
