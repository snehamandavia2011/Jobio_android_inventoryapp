package notification;

import android.support.v4.app.NotificationCompat;

import com.lab360io.jobio.officeApp.ApplicationOffice;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONObject;

import java.math.BigInteger;

import utility.Logger;

/**
 * Created by SAI on 11/28/2016.
 */
public class NotificationExtender extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Logger.debug("in NotificationExtenderService");
        JSONObject data = receivedResult.payload.additionalData;
        Notification.performActionWhileNotificationReceive(data, ApplicationOffice.getContext());
        return true;
    }
}

