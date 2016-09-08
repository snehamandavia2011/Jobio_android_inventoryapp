package entity;

import android.content.Context;

import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 1/2/2016.
 */
public class ClientLocationTrackingInterval {
    private int period;

    public ClientLocationTrackingInterval(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public class Fields{
        public static final String LOCATION_TRACKING_INTERVAL="location_tracking_interval";
    }

    public void saveFiledsToPreferences(Context c){
        Helper.setIntPreference(c, Fields.LOCATION_TRACKING_INTERVAL, this.getPeriod());
    }

    public static void clearCache(Context c) {
        Helper.clearPreference(c, Fields.LOCATION_TRACKING_INTERVAL);
    }

    public void display() {
        Logger.debug(".....................ClientLocationTrackingInterval...........................");
        Logger.debug("Interval value:" + period);
    }
}
