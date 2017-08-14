package com.cruz.bootcamplocator;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Acer on 14/08/2017.
 */

public class Bootcamp implements Parcelable{
    private String name;
    private String address;
    private String pictureUrl;
    private LatLng latLng;

    public Bootcamp() {
    }

    public Bootcamp(String name, String address, String pictureUrl, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.pictureUrl = pictureUrl;
        this.latLng = latLng;
    }

    protected Bootcamp(Parcel in) {
        name = in.readString();
        address = in.readString();
        pictureUrl = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Bootcamp> CREATOR = new Creator<Bootcamp>() {
        @Override
        public Bootcamp createFromParcel(Parcel in) {
            return new Bootcamp(in);
        }

        @Override
        public Bootcamp[] newArray(int size) {
            return new Bootcamp[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(pictureUrl);
        parcel.writeParcelable(latLng, i);
    }
}
