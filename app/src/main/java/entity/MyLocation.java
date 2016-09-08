package entity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import utility.Logger;

/**
 * Created by SAI on 1/5/2016.
 */
public class MyLocation {
    double latitude, longitude;
    String gpsType;

    public MyLocation(double latitude, double longitude, String gpsType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.gpsType = gpsType;
    }

    public void display() {
        Logger.debug("latitude:" + latitude);
        Logger.debug("longitude:" + longitude);
        Logger.debug("gpsType:" + gpsType);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGpsType() {
        return gpsType;
    }

    public void setGpsType(String gpsType) {
        this.gpsType = gpsType;
    }

    public String getAddressFromLocation(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "";
        } finally {
            if (result == null) {
                result = "Unable to get location";
            }
        }
        Logger.debug("Result:" + result);
        return result;
    }
}
