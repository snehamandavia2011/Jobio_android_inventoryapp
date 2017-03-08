package utility;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lab360io.jobio.officeApp.R;

import entity.MyLocation;

public class GPSTracker implements LocationListener {
    GPSTracker objGPSTracker;
    Handler handler;
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null;
    MyLocation objMyLocation = null; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context, Handler handler) {
        this.handler=handler;
        this.mContext = context;
        objGPSTracker = this;
    }

    public MyLocation getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, objGPSTracker);
                                Logger.debug("GPS Enabled");
                            }
                        });
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                objMyLocation = new MyLocation(latitude, longitude, "GPS");
                            }
                        }
                    }
                }
                // First get location from Network Provider
                else if (isNetworkEnabled) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, objGPSTracker);
                        }
                    });

                    Logger.debug("Network Enable");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            objMyLocation = new MyLocation(latitude, longitude, "A-GPS");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objMyLocation;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        final ConfimationSnackbar snackbar = new ConfimationSnackbar((AppCompatActivity) mContext,ConstantVal.ToastBGColor.WARNING);
        snackbar.showSnackBar(mContext.getString(R.string.msgGPSNotAvailable), mContext.getString(R.string.strYes), mContext.getString(R.string.strNo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismissSnackBar();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        }, null);

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}

/*
package utility;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import entity.ClientFieldLocation;
import entity.MyLocation;




public class GPSTracker {
    private final Context mContext;

    public GPSTracker(Context context) {
        this.mContext = context;
    }

    public void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.mContext.sendBroadcast(intent);

        String provider = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.mContext.sendBroadcast(poke);
        }
    }

    // automatic turn off the gps
    public void turnGPSOff() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        this.mContext.sendBroadcast(intent);

        String provider = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.mContext.sendBroadcast(poke);
        }
    }

    public Location getLocationNetWork(LocationManager locationManager) {
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        Location location = null;
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0, 0, listener);
        Logger.debug("Network Enabled");
        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.removeUpdates(listener);
        }
        return location;
    }

    public Location getLocationGPS(LocationManager locationManager) {
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        Location location = null;
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 0, listener);
        Logger.debug("GPS Enabled");
        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.removeUpdates(listener);
        }
        return location;
    }

    public MyLocation getLastLocation() {
        //this.turnGPSOn();
        try {
            LocationManager locationManager = (LocationManager) mContext
                    .getSystemService(mContext.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Logger.debug("isGPSEnabled:" + isGPSEnabled + " isNetworkEnabled:" + isNetworkEnabled);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    Location location = this.getLocationNetWork(locationManager);
                    if(location!=null) {
                        MyLocation objMyLocation = new MyLocation(location.getLatitude(),location.getLongitude
                                (),"A-GPS");
                        return  objMyLocation;
                    }
                }else if (isGPSEnabled) {
                    Location location = this.getLocationGPS(locationManager);
                    if(location!=null) {
                        MyLocation objMyLocation = new MyLocation(location.getLatitude(),location.getLongitude
                                (),"GPS");
                        return  objMyLocation;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.turnGPSOff();
        return null;
    }
}

 */